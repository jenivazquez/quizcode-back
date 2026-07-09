package com.quizcode.module.question.infrastructure.adapter;

import com.quizcode.error.exception.AutoGenerationExceptionCustom;
import com.quizcode.module.question.domain.port.QuestionToAiPort;
import com.quizcode.module.question.domain.entity.option.AIOption;
import com.quizcode.module.question.domain.entity.message.AIMessage;
import com.quizcode.module.question.domain.entity.question.AIQuestion;
import com.quizcode.module.question.domain.entity.question.Question;
import com.quizcode.module.question.domain.entity.type.QuestionType;
import com.quizcode.shared.Util;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class AIQuestionAdapter implements QuestionToAiPort {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = """
        
        Eres un asistente conversacional especializado en generar preguntas para cuestionarios de programación.

        IDIOMA: Todo el contenido debe estar en español: enunciados, opciones, mensajes y código comentado. Nunca uses inglés.

        CUÁNDO GENERAR UNA PREGUNTA: 
        Solo cuando el usuario pide explícitamente una pregunta nueva. Si el usuario pide una aclaración, pide saber dónde está el error, pide una pista o cualquier otra cosa que no sea una pregunta nueva, responde solo con "message" y deja el resto de campos a null. No inventes una pregunta para rellenar los campos.
        Si el usuario te pide que modifiques solo una parte dela pregunta (por ejemplo la puntuación, el enunciado, las opciones, etc), envía solo esos datos concretos que te pide, junto con el message adecuado respondiendo.

        TIPOS DE PREGUNTA:
        - Opción única (SINGLE_CHOICE): tipo test con 1 sola opción correcta. Entre 2 y 6 opciones. baseCode opcional.
        - Opción múltiple (MULTIPLE_CHOICE): tipo test con 1 o más opciones correctas. Entre 3 y 6 opciones. baseCode opcional.
        - Modificación de código (EDIT_CODE): el alumno debe modificar, corregir o completar código. baseCode OBLIGATORIO, options debe ser null. El baseCode NO puede contener ningún comentario de ningún tipo (ni // ni /* */). El código debe presentarse exactamente como lo escribiría un programador, sin indicar ni insinuar dónde está el error ni cuál es la solución.

        Responde SIEMPRE con un JSON con esta estructura exacta, sin texto extra fuera del JSON:
        {
            "message": "Es la respuesta que recibe el usuario en el chat. Debe responder de forma natural al último mensaje recibido por el usuario. Si has generado pregunta y es de tipo EDIT_CODE, incluye aquí cuál es el error, además de empezar el mansaje indicando que la pregunta ha sido creada",
            "statement": "enunciado de la pregunta en español",
            "type": "SINGLE_CHOICE, MULTIPLE_CHOICE o EDIT_CODE",
            "score": puntuación máxima que puede tener la pregunta (número entero, mínimo 0),
            "baseCode": "Código de apoyo a la pregunta. Obligatorio en EDIT_CODE, opcional en el resto.",
            "options": Lista de opciones de respuesta para SINGLE_CHOICE y MULTIPLE_CHOICE con el siguiente formato:
            [
              { "value": El texto de la opción, es lo que ve el usuario
              "isValid": Indica si esta opcion es correcta o incorrecta. Para SINGLE_CHOICE solo puede haber una opción con true, pero el MULTIPLE_CHOICE puede haber 1 o más }
            ]
        }
        """;

    public AIQuestionAdapter(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public AIQuestion generateQuestion(List<Question> savedQuestions, List<AIMessage> messages) {
        AIQuestionJson response = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(getMessages(savedQuestions, messages))
                .call()
                .entity(AIQuestionJson.class);
        return getAIQuestion(response);
    }

    private AIQuestion getAIQuestion(AIQuestionJson response) {
        try {
            QuestionType type = response.type != null ? QuestionType.valueOf(response.type) : null;
            List<AIOption> options = !Util.isNull(response.options) ? response.options.stream().map(o -> new AIOption(o.value, o.isValid)).toList() : null;
            return new AIQuestion(response.message, response.statement, type, response.score, response.baseCode, options);
        } catch (Exception e) {
            throw new AutoGenerationExceptionCustom("La IA no devolvió una respuesta con el formato esperado");
        }
    }

    private List<Message> getMessages(List<Question> savedQuestions, List<AIMessage> messages) {

        List<Message> finalMessages = new ArrayList<>();

        if (!Util.isNull(savedQuestions)) {
            finalMessages.add(new UserMessage(buildSavedQuestionsMessage(savedQuestions)));
            finalMessages.add(new AssistantMessage("ok"));
        }

        finalMessages.addAll(messages.stream().map(m ->
                        "user".equals(m.role())
                        ? new UserMessage(m.content())
                        : new AssistantMessage(m.content()))
                .toList());

        return finalMessages;
    }

    private String buildSavedQuestionsMessage(List<Question> savedQuestions) {
        String list = IntStream.range(0, savedQuestions.size())
                .mapToObj(i -> (i + 1) + ". \"" + savedQuestions.get(i).getStatement() + "\" (" + savedQuestions.get(i).getType() + ")")
                .collect(Collectors.joining("\n"));
        return "El cuestionario ya tiene estas preguntas. No generes duplicados ni preguntas muy similares:\n" + list;
    }

    private record AIQuestionJson (String message, String statement, String type, Integer score, String baseCode, List<AIOptionJson> options){}

    private record AIOptionJson (String value, Boolean isValid){}
}
