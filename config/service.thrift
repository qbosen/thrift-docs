include "other.thrift"

/**
* thrift接口文档demo
*
* 换行
*/
namespace java thrift

/**服务接口*/
service Service{
    /**
    * 获取指定时间范围内
    * 内容统计信息
    **/
    other.Statistics getStatistics(
    /** app签名*/
    1:string appSign,
    /** 开始时间，秒为单位*/
    2:i64 start,
    /** 结束时间，秒为单位*/
    3:i64 stop);
}