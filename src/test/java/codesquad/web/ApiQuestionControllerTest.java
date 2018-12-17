package codesquad.web;

import codesquad.domain.Question;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.slf4j.LoggerFactory.getLogger;

public class ApiQuestionControllerTest extends AcceptanceTest {
    private static final Logger log = getLogger(ApiQuestionControllerTest.class);

    @Test
    public void create() {
        Question newQuestion = new Question("테스트 질문1", "테스트 내용1");
        String location = createResource("/api/questions", newQuestion);
        Question dbQuestion = template().getForObject(location, Question.class);
        softly.assertThat(dbQuestion).isNotNull();
    }

    @Test
    public void create_no_login() {
        Question newQuestion = new Question("테스트 질문2", "테스트 내용2");
        ResponseEntity<String> response = template().postForEntity("/api/questions", newQuestion, String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        log.debug("errorMessage : {}", response.getBody());
    }
}