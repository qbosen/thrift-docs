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

  生成名叫`official-test`的`thrift文档`，因为生成的html文件名是thrift的文件名，为了方便静态资源映射，结合现有业务，我们约定文件名为`services.thrift`
  可在配置中修改: **docs.file**

  这里是用的[官方文件](https://raw.githubusercontent.com/apache/thrift/master/test/ThriftTest.thrift)

  `/opt/thrift-docs/config/docs-api gen offical-test ./services.thrift`

* 查看文档

  docs地址 http://localhost:9012/, 结合上面的官方文件，可以了解文档的编写方式

## PS

  这里提供了一个macOS,0.9.2的版本，可以config文件下生成本级thrift的软连接