namespace java thrift.other

/** 内容统计*/
struct Statistics{
    /**发布内容数*/
    1:i64 contentCount;
    /**浏览数*/
    2:i64 viewCount;
    /**真实浏览数*/
    3:i64 realViewCount;
}