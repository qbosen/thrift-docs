package top.abosen.toys.thrift_docs

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.ApplicationPidFileWriter
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import java.io.File

/**
 * @author qiubaisen
 * @date 2020/9/1
 */


fun main(args: Array<String>) {
    runApplication<ThriftDocsApplication>(*args) {
        addListeners(ApplicationPidFileWriter())
    }
}

@SpringBootApplication
@EnableConfigurationProperties(DocsConfiguration::class)
class ThriftDocsApplication : ApplicationRunner {
    @Autowired
    lateinit var config: DocsConfiguration
    override fun run(args: ApplicationArguments?) {
        logger.info("配置为: $config")
        with(File(config.dir)){
            if(exists().not()){
                mkdirs()
            }
        }
    }

    companion object {
        val logger = logger()
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "docs")
data class DocsConfiguration(
        val dir: String,
        val file:String,
        var cacheSeconds: Long
)

inline fun <reified T> T.logger(): Logger {
    if (T::class.isCompanion) {
        return LoggerFactory.getLogger(T::class.java.enclosingClass)
    }
    return LoggerFactory.getLogger(T::class.java)
}
