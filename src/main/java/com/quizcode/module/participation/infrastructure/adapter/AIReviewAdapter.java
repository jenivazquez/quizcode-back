package com.quizcode.module.participation.infrastructure.adapter;

import com.quizcode.module.participation.domain.AIReviewPort;
import com.quizcode.module.participation.domain.entity.ai.AIReview;
import com.quizcode.shared.Util;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class AIReviewAdapter implements AIReviewPort {

    private static final int MAX_FEEDBACK_LENGTH = 500;

    private final ChatClient chatClient;

    public AIReviewAdapter(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    private static final String SYSTEM_PROMPT =
            "Eres un evaluador de código para un cuestionario de programación. " +
            "Evalúa la respuesta del participante de forma objetiva.\n\n" +
            "Para corregir la respuesta se debe hacer una comparación entre el código base proporcionado y la respuesta del participante, viendo lo que ha cambiado.\n\n" +
            "Si la respuesta está mal (no funciona o no hace lo que se pide), la puntuación será 0 y el campo isCorrect será false.\n" +
            "Si la respuesta está bien (funciona y hace lo que se pide), la puntuación será la máxima y el campo isCorrect será true.\n" +
            "Cuando la respuesta está bien, puede asignarse también una puntuación intermedia (entre el mínimo y el máximo) " +
            "si se considera que la calidad del código es baja, pero el campo isCorrect seguirá siendo true.\n\n" +
            "Proporciona un feedback en español de máximo 500 caracteres, tanto si la respuesta es correcta como incorrecta.\n" +
            "El feedback debe hacer referencia únicamente a la corrección de la respuesta y debe proporcionar la respuesta correcta en caso de error.\n" +
            "El feedback debe ser claro y conciso.";

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 30000, multiplier = 2))
    @Override
    public AIReview reviewCode(String statement, String baseCode, String writtenAnswer, int maxScore) {
        AiResponse response = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(buildUserPrompt(statement, baseCode, writtenAnswer, maxScore))
                .call()
                .entity(AiResponse.class);
        return getAIReview(response, maxScore);
    }

    private String buildUserPrompt(String statement, String baseCode, String writtenAnswer, int maxScore) {
        StringBuilder sb = new StringBuilder();
        sb.append("El enunciado de la pregunta es: ").append(statement).append("\n");
        if (!Util.isNull(baseCode)) { sb.append("El código base que se proporciona al participante como parte de la pregunta es: ").append(baseCode).append("\n"); }
        sb.append("La respuesta que da el participante es: ").append(writtenAnswer).append("\n\n");
        sb.append("Asigna una puntuación entera de 0 a ").append(maxScore).append(".");
        return sb.toString();
    }

    private AIReview getAIReview(AiResponse response, int maxScore) {
        int score = Math.min(Math.max(response.score(), 0), maxScore);
        String feedback = !Util.isNull(response.feedback()) && response.feedback().length() > MAX_FEEDBACK_LENGTH
                ? response.feedback().substring(0, MAX_FEEDBACK_LENGTH)
                : response.feedback();
        return new AIReview(response.isCorrect(), score, feedback);
    }

    private record AiResponse(boolean isCorrect, int score, String feedback) {}
}