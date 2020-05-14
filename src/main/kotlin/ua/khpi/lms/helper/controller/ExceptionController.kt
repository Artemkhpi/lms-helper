package ua.khpi.lms.helper.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import ua.khpi.lms.helper.exception.ApiException


@Controller
class ExceptionController {

    @ExceptionHandler(ApiException::class)
    fun handleApiException(model: Model, exception: ApiException): String {
        model.addAttribute("message", exception.message)
        return "error"
    }

}