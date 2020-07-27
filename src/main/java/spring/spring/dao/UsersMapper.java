package spring.spring.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import spring.spring.dao.entity.Users;

@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}
