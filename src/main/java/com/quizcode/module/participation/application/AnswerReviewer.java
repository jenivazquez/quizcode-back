package com.quizcode.module.participation.application;

import com.quizcode.module.participation.domain.AIReviewPort;
import com.quizcode.module.participation.domain.ParticipationRepository;
import com.quizcode.module.participation.domain.entity.ai.AIReview;
import com.quizcode.module.participation.domain.entity.answer.Answer;
import com.quizcode.module.participation.domain.entity.answer.ReviewedAnswer;
import com.quizcode.module.participation.domain.entity.question.QuestionSummary;
import com.quizcode.module.participation.domain.entity.question.QuestionType;
import com.quizcode.module.participation.domain.entity.status.ReviewStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AnswerReviewer {

    private final AIReviewPort aiReviewPort;
    private final ParticipationRepository partRepository;

    public AnswerReviewer(AIReviewPort aiReviewPort, ParticipationRepository partRepository) {
        this.aiReviewPort = aiReviewPort;
        this.partRepository = partRepository;
    }

    @Async
    public void reviewAnswersAsync(String participationId, List<Answer> answers, List<QuestionSummary> questions) {
        List<Answer> reviewedAnswers = reviewAnswers(answers, questions);
        int totalScore = reviewedAnswers.stream().mapToInt(a -> a.getScore() != null ? a.getScore() : 0).sum();
        partRepository.updateReviewAnswers(participationId, reviewedAnswers, totalScore, ReviewStatus.IA_REVIEWED);
    }

    public List<Answer> reviewAnswers(List<Answer> answers, List<QuestionSummary> questions) {
        Map<String, QuestionSummary> questionMap = questions.stream().collect(Collectors.toMap(QuestionSummary::getId, q -> q));
        List<CompletableFuture<Answer>> futures = answers.stream()
                .map(answer -> CompletableFuture.supplyAsync(() -> reviewAnswer(answer, questionMap.get(answer.getQuestionId())))
                        .exceptionally(e -> { log.error("Error al corregir la pregunta {}: {}", answer.getQuestionId(), e.getMessage()); return answer; }))
                .toList();
        return futures.stream().map(CompletableFuture::join).toList();
    }

    private Answer reviewAnswer(Answer answer, QuestionSummary question) {
        return switch (question.getType()) {
            case EDIT_CODE -> reviewCodeAnswer(answer, question);
            case MULTIPLE_CHOICE -> reviewMultipleOptionAnswer(answer, question);
            case SINGLE_CHOICE -> reviewSingleOptionAnswer(answer, question);
        };
    }

    private Answer reviewSingleOptionAnswer(Answer answer, QuestionSummary question) {
        boolean isCorrect = new HashSet<>(question.getValidOptionCodes()).equals(new HashSet<>(answer.getCodeOptions()));
        int score = isCorrect ? question.getScore() : 0;
        return new ReviewedAnswer(answer.getQuestionId(), isCorrect, score, null).getAnswer();
    }

    private Answer reviewMultipleOptionAnswer(Answer answer, QuestionSummary question) {
        Set<String> validOptions = new HashSet<>(question.getValidOptionCodes());
        Set<String> selectedOptions = new HashSet<>(answer.getCodeOptions());

        long rightAnswers = selectedOptions.stream().filter(validOptions::contains).count();
        long failedAnswers = selectedOptions.stream().filter(o -> !validOptions.contains(o)).count();

        boolean isCorrect = rightAnswers == validOptions.size() && failedAnswers == 0;
        int score = Math.max(0, Math.round((float) (rightAnswers - failedAnswers) / validOptions.size() * question.getScore()));
        return new ReviewedAnswer(answer.getQuestionId(), isCorrect, score, null).getAnswer();
    }

    private Answer reviewCodeAnswer(Answer answer, QuestionSummary question) {
        AIReview aiReview = aiReviewPort.reviewCode(question.getStatement(), question.getBaseCode(), answer.getWrittenAnswer(), question.getScore());
        return new ReviewedAnswer(answer.getQuestionId(), aiReview.isCorrect(), aiReview.getScore(), aiReview.getFeedback()).getAnswer();
    }
}