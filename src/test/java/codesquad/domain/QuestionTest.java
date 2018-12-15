package codesquad.domain;

import codesquad.CannotDeleteException;
import codesquad.UnAuthorizedException;
import org.junit.Test;
import support.test.BaseTest;

import static codesquad.domain.UserTest.BRAD;
import static codesquad.domain.UserTest.JUNGHYUN;

public class QuestionTest extends BaseTest {
    public static final Question question = new Question(1L, "제목 테스트", "내용 테스트 - 코드스쿼드 qna-atdd step2 진행중입니다", BRAD);
    public static final Question question2 = new Question(2L, "제목 테스트2", "내용 테스트2 - 전혀 다른 내용입니다.", JUNGHYUN);
    public static final Question updatedQuestion = new Question("업데이트 제목", "업데이트 컨텐츠");

    @Test
    public void update_succeed() {
        Question newQuestion = question.update(BRAD, updatedQuestion);
        softly.assertThat(newQuestion.getTitle()).isEqualTo(updatedQuestion.getTitle());
        softly.assertThat(newQuestion.getContents()).isEqualTo(updatedQuestion.getContents());
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_not_same_writer() {
        question.update(JUNGHYUN, updatedQuestion);
    }

    @Test(expected = CannotDeleteException.class)
    public void delete_not_same_writer() throws CannotDeleteException {
        question.delete(JUNGHYUN);
    }

    @Test
    public void delete_succeed() throws CannotDeleteException {
        question.delete(BRAD);
        softly.assertThat(question.isDeleted()).isEqualTo(true);
    }
}