package codesquad.web;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;
import support.test.HtmlFormDataBuilder;

import static codesquad.domain.QuestionTest.QUESTION;
import static codesquad.domain.QuestionTest.UPDATED_QUESTION;
import static org.slf4j.LoggerFactory.getLogger;

public class QuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = getLogger(QuestionAcceptanceTest.class);
    HttpEntity<MultiValueMap<String, Object>> testRequest;
    HttpEntity<MultiValueMap<String, Object>> updateRequest;
    HttpEntity<MultiValueMap<String, Object>> deleteRequest;

    @Before
    public void setUp() throws Exception {
        testRequest = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("title", QUESTION.getTitle())
                .addParameter("contents", QUESTION.getContents())
                .build();

        updateRequest = HtmlFormDataBuilder.urlEncodedForm().put()
                .addParameter("title", UPDATED_QUESTION.getTitle())
                .addParameter("contents", UPDATED_QUESTION.getContents())
                .build();

        deleteRequest = HtmlFormDataBuilder.urlEncodedForm().delete().build();
    }

    @Test
    public void question_form_not_login() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void question_form_login() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).getForEntity("/questions/form", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void create_question_not_login() throws Exception {
        ResponseEntity<String> response = template().postForEntity("/questions", testRequest, String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void create_question_login() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).postForEntity("/questions", testRequest, String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        softly.assertThat(response.getHeaders().getLocation().getPath()).startsWith("/questions/");
    }

    @Test
    public void show_created_question() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/questions/1", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void show_not_created_question() {
        ResponseEntity<String> response = template().getForEntity("/questions/100", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void home() {
        ResponseEntity<String> response = template().getForEntity("/", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void update_form_not_login() {
        ResponseEntity<String> response = template().getForEntity("/questions/1/form", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void update_form_not_same_writer() {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).getForEntity("/questions/2/form", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void update_form_same_writer() {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).getForEntity("/questions/1/form", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        softly.assertThat(response.getBody()).contains("수정하기");
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void update_not_login() {
        ResponseEntity<String> response = template().postForEntity("/questions/1", updateRequest, String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void update_not_same_writer() {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).postForEntity("/questions/2", updateRequest, String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void update_succeed() {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).postForEntity("/questions/1", updateRequest, String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        softly.assertThat(response.getBody()).contains(UPDATED_QUESTION.getTitle());
        softly.assertThat(response.getBody()).contains(UPDATED_QUESTION.getContents());
    }

    @Test
    public void delete_not_login() {
        ResponseEntity<String> response = template().postForEntity("/questions/1", deleteRequest, String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void delete_not_same_writer() {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).postForEntity("/questions/2", deleteRequest, String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete_succeed() {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).postForEntity("/questions/1", deleteRequest, String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        softly.assertThat(response.getHeaders().getLocation().getPath()).startsWith("/");
    }
}
