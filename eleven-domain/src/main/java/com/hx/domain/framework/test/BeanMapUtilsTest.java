package com.hx.domain.framework.test;

import java.text.ParseException;
import java.util.*;

public class BeanMapUtilsTest {


    public static void main(String[] args) throws ParseException {
//        CronExpression cronExpression = new CronExpression("0 0/30 * * * * ?");
//        cronExpression.
//        Map<String, Object> map = map();
//        CustomerDTO dto = getCustomerDTO();
//        Map<String, Object> beanMap = BeanMapUtil.beanToMap(dto);
//        Object customerDTO = BeanMapUtil.mapToBean(map, CustomerDTO.class);
//        Object customerDTO1 = BeanUtils.copyProperties(map, CustomerDTO.class);
//        System.out.println("size:" + map.size());
    }

    private static Map<String, Object> map(){
        Map<String, Object> map = new HashMap<>();
        map.put("destUserId", "0001111");
        map.put("destAppId", "APP1106");
        map.put("secTxCode", "fdsafsd");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("procedureStep", "第一步");
        map1.put("sourceUserId", true);
        map1.put("sourceAppId", 2201);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("procedureStep", "第二步");
        map2.put("sourceUserId",true);
        map2.put("sourceAppId", 2202);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("procedureStep", "第三步");
        map3.put("sourceUserId", true);
        map3.put("sourceAppId", 2203);

        Map<String, Object> dmap1 = new HashMap<>();
        dmap1.put("gid", 1542145L);
        dmap1.put("fnum", 12.254f);
        dmap1.put("money", 23.54);
        dmap1.put("ffnums", 5556.08f);
        dmap1.put("trues", false);
        dmap1.put("totale", 888888888888.88);
        Map<String, Object> dmap2 = new HashMap<>();
        dmap2.put("gid", 1542145L);
        dmap2.put("fnum", 12.254f);
        dmap2.put("money", 23.54);
        dmap2.put("ffnums", 5556.08f);
        dmap2.put("trues", false);
        dmap2.put("totale", 888888888889.88);
        Map<String, Object> dmap3 = new HashMap<>();
        dmap3.put("gid", 1542145L);
        dmap3.put("fnum", 12.254f);
        dmap3.put("money", 23.54);
        dmap3.put("ffnums", 5556.08f);
        dmap3.put("trues", false);
        dmap3.put("totale", 888888888888.98);

        map1.put("demoDTO", dmap1);
        map2.put("demoDTO", dmap2);
        map3.put("demoDTO", dmap3);

        List<Map<String, Object>> listMap = new ArrayList<>();
        listMap.add(map2);
        listMap.add(map3);
        map.put("customerChannelDTO", map1);
        map.put("list", listMap);
        return map;
    }

    private static CustomerDTO getCustomerDTO(){
        CustomerDTO dto = new CustomerDTO("0001111", "APP1106", "fdsafsd");
        CustomerChannelDTO customerChannelDTO = new CustomerChannelDTO("第一步", true, 2201);
        CustomerChannelDTO customerChannelDTO1 = new CustomerChannelDTO("第二步", true, 2202);
        CustomerChannelDTO customerChannelDTO2 = new CustomerChannelDTO("第三步", true, 2203);
        DemoDTO demoDTO1 = new DemoDTO(1542145L,12.254f,23.54,5556.08f,false,888888888888.88);
        DemoDTO demoDTO2 = new DemoDTO(1542145L,12.254f,23.54,5556.08f,false,888888888889.88);
        DemoDTO demoDTO3 = new DemoDTO(1542145L,12.254f,23.54,5556.08f,false,888888888889.98);
        customerChannelDTO.setDemoDTO(demoDTO1);
        customerChannelDTO1.setDemoDTO(demoDTO2);
        customerChannelDTO2.setDemoDTO(demoDTO3);
        dto.setCustomerChannelDTO(customerChannelDTO);
        List<CustomerChannelDTO> list = new ArrayList<>();
        list.add(customerChannelDTO1);
        list.add(customerChannelDTO2);
        dto.setList(list);
        return dto;
    }
}
