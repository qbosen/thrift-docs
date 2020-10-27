package top.abosen.toys.thrift_docs

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File
import java.nio.file.Paths
import javax.servlet.http.HttpServletRequest


/**
 * @author qiubaisen
 * @date 2020/9/1
 */

@Controller
class WebController(private val configuration: DocsConfiguration) {
    @GetMapping("/")
    fun index(model: Model): String {
        val services = File(configuration.dir).listFiles()?.asSequence()?.filter { it.isDirectory }
                ?.map { ServiceModel(it.name, "dir/${it.name}") }?.toList() ?: emptyList()
        model.addAttribute("services", services)
        return "docsPage"
    }

    @GetMapping("/dir")
    fun redirect1(): String {
        return "redirect:/"
    }

    @GetMapping("/dir/")
    fun redirect2(): String {
        return "redirect:/"
    }


    @GetMapping("/dir/**")
    fun path(request: HttpServletRequest, model: Model): String {
        val path = request.requestURI.split("/dir/")[1]
        val target = Paths.get(configuration.dir, path).toFile()
        if (target.exists() && target.isDirectory) {
            if (target.listFiles { _, name -> name == configuration.file }.isNullOrEmpty().not()) {
                // 目标文件夹
                return "redirect:/docs/$path/${configuration.file}"
            }
        }
        // 是目录
        val dirs = target.listFiles()?.filter { it.isDirectory }?.map { ServiceModel(it.name, "/dir/$path/${it.name}") }
                ?: emptyList()
        model.addAttribute("services", dirs)
        return "docsPage"
    }
}

data class ServiceModel(val name: String, val path: String)

@Configuration
class WebConfigure(private val configuration: DocsConfiguration) : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/docs/**").addResourceLocations("file:${configuration.dir}")
    }
}