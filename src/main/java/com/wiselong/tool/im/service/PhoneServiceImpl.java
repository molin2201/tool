package com.wiselong.tool.im.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wiselong.tool.im.bean.PhoneEntity;
import com.wiselong.tool.im.dao.PhoneDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PhoneServiceImpl extends ServiceImpl<PhoneDao, PhoneEntity> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void batchUpdateIsImport(List<PhoneEntity> ids) {
        String sql=" update phone set is_import='Y' where id = ? ";
        List<Long> id=new ArrayList<Long>();
        for(PhoneEntity phoneEntity:ids){

            id.add(phoneEntity.getId());
        }
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, id.get(i));
            }
            @Override
            public int getBatchSize() {
                return id.size();
            }
        });
    }
    public void saveThirdBlack(String phone){
        String sql = "INSERT INTO third_phone(`phone`,`is_import`) VALUES(?,'Y')";
          jdbcTemplate.update(sql,phone);
    }
    public List<PhoneEntity> queryBySize(String isImport, Integer size) {

        String sql = "select * from phone where `is_import`='N' limit 0, ?";
        return  jdbcTemplate.query(sql,new Object[]{size},new BeanPropertyRowMapper<>(PhoneEntity.class));
//        PhoneEntity condition = new PhoneEntity();
//        condition.setIsImport("N");
//        QueryWrapper<PhoneEntity>   queryWrapper = new QueryWrapper<>(condition);
//
//        Page<PhoneEntity> page=this.baseMapper.selectPage(new Page<>(1,size),queryWrapper);
//        if(page!=null ){
//            return   page.getRecords();
//        }
//        return null;
    }
}
