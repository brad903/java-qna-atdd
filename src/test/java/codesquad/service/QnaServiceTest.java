package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import support.test.BaseTest;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static codesquad.domain.QuestionTest.*;
import static codesquad.domain.UserTest.BRAD;
import static codesquad.domain.UserTest.JUNGHYUN;
import static org.mockito.Mockito.when;
import static org.slf4j.LoggerFactory.getLogger;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest extends BaseTest {
    private static final Logger log = getLogger(QnaServiceTest.class);

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QnaService qnaService;

    @Before
    public void setUp() throws Exception {
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
    }

    @Test
    public void update_succeed() {
        Question newQuestion = qnaService.update(BRAD, question.getId(), updatedQuestion);
        softly.assertThat(newQuestion.getContents()).isEqualTo(updatedQuestion.getContents());
        softly.assertThat(newQuestion.getTitle()).isEqualTo(updatedQuestion.getTitle());
    }

    @Test(expected = EntityNotFoundException.class)
    public void update_cannot_found_question() {
        qnaService.update(BRAD, question2.getId(), updatedQuestion);
    }

    @Test
    public void delete_succeed() throws CannotDeleteException {
        qnaService.deleteQuestion(BRAD, question.getId());
        softly.assertThat(question.isDeleted()).isEqualTo(true);
    }

    @Test(expected = EntityNotFoundException.class)
    public void delete_cannot_found_question() throws CannotDeleteException {
        qnaService.deleteQuestion(BRAD, question2.getId());
    }

    @Test(expected = CannotDeleteException.class)
    public void delete_not_same_writer() throws CannotDeleteException {
        qnaService.deleteQuestion(JUNGHYUN, question.getId());
    }


}