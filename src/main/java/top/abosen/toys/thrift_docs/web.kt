package top.abosen.toys.thrift_docs

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File
import java.nio.file.Paths
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import kotlin.io.path.Path


/**
 * @author qiubaisen
 * @date 2020/9/1
 */

@Controller
class WebController(private val configuration: DocsConfiguration) {
    private val serviceDocHolder = ServiceDocHolder(configuration)

    @GetMapping("/refresh")
    fun docsPage(model: Model): String {
        model.addAttribute("services", serviceDocHolder.getService(true))
        return "docsPage"
    }

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

data class ServiceDoc(val name: String, val path: String, var isDoc: Boolean, val children: MutableList<ServiceDoc>)
class ServiceDocHolder(private val configuration: DocsConfiguration) {
    private var updateTime = LocalDateTime.now()
    private var docs: List<ServiceDoc> = loadServices()

    fun getService(flush: Boolean = false): List<ServiceDoc> {
        if (flush || updateTime.plusSeconds(configuration.cacheSeconds).isBefore(LocalDateTime.now())) {
            docs = loadServices()
            updateTime = LocalDateTime.now()
        }
        return docs
    }

    private fun loadServices(): List<ServiceDoc> {
        val dummy = ServiceDoc("", "", false, mutableListOf())
        val workdir = Path(configuration.dir)
        val thriftDocTrait = setOf("index.html", "style.css")
        fun doLoad(node: ServiceDoc) {
            if (node.isDoc) return
            val children = (workdir.resolve(node.path).toFile().listFiles()?.asList()
                ?.map { ServiceDoc(it.name, "${Path(node.path, it.name)}", !it.isDirectory, mutableListOf()) }
                ?: emptyList())

            // 包含这些文件的目录被视作文档
            if (children.map { it.name }.containsAll(thriftDocTrait)) {
                // 除了 index 和 style 的html文件, 就是模块文档
                val moduleDocs =
                    children.filter { it.isDoc && !thriftDocTrait.contains(it.name) && it.name.endsWith(".html") }
                node.children.addAll(moduleDocs)
                node.isDoc = true
                return
            }
            node.children.addAll(children)
            node.children.forEach { doLoad(it) }
        }
        doLoad(dummy)
        return dummy.children
    }
}

@Configuration
class WebConfigure(private val configuration: DocsConfiguration) : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/docs/**").addResourceLocations("file:${configuration.dir}")
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/")
    }
}