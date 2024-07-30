package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
public class ShopController {

    public static final String ShOP_STATUS = "SHOP_STATUS";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(ShOP_STATUS);
        log.info("获取店铺营业状态为:{}",status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }

}
