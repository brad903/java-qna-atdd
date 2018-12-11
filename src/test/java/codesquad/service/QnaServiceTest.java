package codesquad.service;

import codesquad.UnAuthorizedException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
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

import static org.mockito.Mockito.when;
import static org.slf4j.LoggerFactory.getLogger;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest extends BaseTest {
    private static final Logger log = getLogger(QnaServiceTest.class);
    public static final long QUESTION_ID = 1L;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QnaService qnaService;

    User user;
    Question question;
    Question modifiedQuestion;

    @Before
    public void setUp() throws Exception {
        user = new User("brad903", "1234", "Brad", "brad903@naver.com");
        user.setId(1L);
        question = new Question("제목 테스트", "내용 테스트 - 코드스쿼드 qna-atdd step2 진행중입니다");
        question.writeBy(user);
        question.setId(QUESTION_ID);
        modifiedQuestion = new Question("업데이트된 제목", "업데이트된 내용입니다");
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
    }

    @Test
    public void update_succeed() {
        Question updatedQuestion = qnaService.update(user, question.getId(), modifiedQuestion);
        softly.assertThat(updatedQuestion.getContents()).isEqualTo(modifiedQuestion.getContents());
        softly.assertThat(updatedQuestion.getTitle()).isEqualTo(modifiedQuestion.getTitle());
    }

    @Test(expected = EntityNotFoundException.class)
    public void update_cannot_found_question() {
        Question updatedQuestion = qnaService.update(user, 27L, modifiedQuestion);
    }
}