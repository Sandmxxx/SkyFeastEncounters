package com.sky.controller.admin;

import com.sky.annotation.AutoFill;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("adminShopController")// 指定bean的名称，防止冲突
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
public class ShopController {

    public static final String ShOP_STATUS = "SHOP_STATUS";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @PutMapping("/{status}")
    @ApiOperation("修改店铺营业状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("修改店铺营业状态为:{}",status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(ShOP_STATUS,status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(ShOP_STATUS);
        log.info("获取店铺营业状态为:{}",status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }

}
