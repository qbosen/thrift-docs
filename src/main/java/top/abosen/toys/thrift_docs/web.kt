package top.abosen.toys.thrift_docs

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File


/**
 * @author qiubaisen
 * @date 2020/9/1
 */

@Controller
class WebController(private val configuration: DocsConfiguration) {

    @GetMapping("/")
    fun index(model: Model): String {
        val services = File(configuration.dir).listFiles()?.asSequence()?.filter { it.isDirectory }
                ?.map { it.name }?.toList() ?: emptyList()
        model.addAttribute("services",
                services)
        return "docsPage"
    }

    @GetMapping("/docs")
    fun docsPage(): String = "redirect:/"

    @GetMapping("/docs/{service}")
    fun viewDocs(@PathVariable service: String): String {
        // 重定向 /docs/demo => /docs/demo/service.html
        return "redirect:$service/${configuration.file}"
    }
}

@Configuration
class WebConfigure(private val configuration: DocsConfiguration) : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/docs/**").addResourceLocations("file:${configuration.dir}")
    }
}