package junktion.v1.presentation

import junktion.v1.api.ImageController
import junktion.v1.api.ImageService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import javax.validation.ConstraintViolationException

@RestController
class ImageControllerImpl (
        private val imageService: ImageService
): ImageController {

    override fun upload(multipartFile: MultipartFile, tag: String) {
        val file = File(multipartFile.resource.uri)
        imageService.upload(file, tag)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleException(): String {
        return "バリデーションエラーです"
    }
}