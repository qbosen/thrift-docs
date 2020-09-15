# thrift文档服务

启动服务和生成文档都是用`docs-api`

```shell
[root@localhost tmp]# /opt/thrift-docs/config/docs-api -h
usage: /opt/thrift-docs/config/docs-api start|stop|restart|status [args]
usage: /opt/thrift-docs/config/docs-api gen [service_name] [thrift_files...]
```

* 本地生成文档
  `thrift --gen html -o [dest_dir] [thrift_files...]`

* 服务端生成文档

  生成名叫`official-test`的`thrift文档`，因为生成的html文件名是thrift的文件名，为了方便静态资源映射，需要指定`service_name`也就是
  生成文件所在的目录名。默认映射路径为该目录下的`index.html`。
  可在配置中修改: **docs.file**

  这里是用的[官方文件](https://raw.githubusercontent.com/apache/thrift/master/test/ThriftTest.thrift)

  `/opt/thrift-docs/config/docs-api gen offical-test ./services.thrift`
* 多个文档

  多个thrift文件互相include时，将主文件(include所有其他的thrift文件)放在最后，用于生成最完整的`index.html`
  
* 查看文档

  docs地址 http://localhost:9012/, 结合上面的官方文件，可以了解文档的编写方式

## PS

  这里提供了一个macOS,0.9.2的版本，可以config文件下生成本级thrift的软连接