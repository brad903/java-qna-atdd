package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.hibernate.annotations.GeneratorType;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    private static final Logger log = getLogger(QuestionController.class);

    @Resource(name = "qnaService")
    private QnaService qnaService;

    @GetMapping("/form")
    public String form(@LoginUser User loginUser) {
        return "qna/form";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser, Question question, Model model) {
        question = qnaService.create(loginUser, question);
        model.addAttribute("question", question);
        return "qna/show";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        Question question = qnaService.findById(id)
                            .orElseThrow(EntityNotFoundException::new);
        model.addAttribute("question", question);
        return "qna/show";
    }


}
