package codesquad.domain;

import codesquad.UnAuthorizedException;
import org.junit.Before;
import org.junit.Test;
import support.test.BaseTest;

public class QuestionTest extends BaseTest {
    User user;
    Question question;
    Question modifiedQuestion;

    @Before
    public void setUp() throws Exception {
        user = new User("brad903", "1234", "Brad", "brad903@naver.com");
        user.setId(1L);
        question = new Question("제목 테스트", "내용 테스트 - 코드스쿼드 qna-atdd step2 진행중입니다");
        question.writeBy(user);
        modifiedQuestion = new Question("업데이트된 제목", "업데이트된 내용입니다");
    }

    @Test
    public void update_success() {
        Question updatedQuestion = question.update(user, modifiedQuestion);
        softly.assertThat(updatedQuestion.getTitle()).isEqualTo(modifiedQuestion.getTitle());
        softly.assertThat(updatedQuestion.getContents()).isEqualTo(modifiedQuestion.getContents());
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_not_writer() {
        User fakeUser = new User("leejh903", "1234", "브래드", "leejh903@gmail.com");
        fakeUser.setId(2L);
        question.update(fakeUser, modifiedQuestion);
    }
}