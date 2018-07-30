package com.blockchain.wallet.api.service;

import java.util.List;
import java.util.Map;

/**
 *
 * @author shujun
 * @date 2018/7/13
 */
public interface SysDictProviderService {

    /**
     * 获取所有字典表
     *
     * @return Map<分区ID, Map<枚举项代码,枚举项名称>
     */
    public Map<String, Map<String, String>> getAllDictMap() ;


    /**
     * 根据分区代码取得对应的HashMap<枚举项代码,枚举项名称>
     *
     * @param partitionId 分区代码
     * @return HashMap<枚举项代码,枚举项名称>
     */
    public Map<String, String> findDictMapByPartitionId(String partitionId) ;


    /**
     * 根据分区代码取得对应的List<字典项Map>
     *
     * @param partitionId 分区代码
     * @return List<字典项Map>
     */
    public List<Map<String, String>> findDictListByPartitionId(String partitionId);

    /**
     * 根据分区代码取得对应的List<字典项Bean>
     *
     * @param partitionId 分区代码
     * @return List<字典项Bean>
     */
    public List findDictBeanListByPartitionId(String partitionId) ;


    /**
     * 根据分区代码、枚举项代码取得枚举项名称
     *
     * @param partitionId 分区代码
     * @param args        枚举项代码,默认枚举项名称
     * @return 枚举项名称
     */
    public String getLabel(String partitionId, String... args) ;



    /**
     * 根据分区代码、层级取得对应的HashMap<枚举项代码,枚举项名称>
     *
     * @param partitionId 分区代码
     * @param level       层级
     * @return HashMap<枚举项代码,枚举项名称>
     */
    public Map<String, String> findDictMapByLevel(String partitionId, String level) ;


    /**
     * 根据分区代码、层级取得对应的List<字典项Map>
     *
     * @param partitionId 分区代码
     * @param level       层级
     * @return HashMap<枚举项代码,枚举项名称>
     */
    public List<Map<String, String>> findDictListByLevel(String partitionId, String level) ;

    /**
     * 根据分区代码、上级代码取得对应的HashMap<枚举项代码,枚举项名称>
     *
     * @param partitionId 分区代码
     * @param parentCode  上级代码
     * @return HashMap<枚举项代码,枚举项名称>
     */
    public Map<String, String> findDictMapByParentCode(String partitionId, String parentCode) ;

    /**
     * 根据分区代码、上级代码取得对应的List<字典项Map>
     *
     * @param partitionId 分区代码
     * @param parentCode  上级代码
     * @return HashMap<枚举项代码,枚举项名称>
     */
    public List<Map<String, String>> findDictListByParentCode(String partitionId, String parentCode) ;



}
