package com.transsnet.bean

import java.util.Date


/**
 *
 * @param member_id
 * @param create_time
 * @param verified_trade
 * @param verified_order_no
 * @param verified_trade_time
 */
case class MakeTag2MemberBean(member_id:String,
                              var create_time:Date,
                              verified_trade:Int,
                              verified_order_no:String,
                              verified_trade_time:Date)

/**
 *
 * @param new_verified_1            层级认证的用户数
 * @param new_verified_2
 * @param new_verified_3
 * @param trading_volume_1          层级激活的用户数
 * @param trading_volume_2
 * @param trading_volume_3
 * @param base_invitees             用户名下所有认证的用户数(历史)
 * @param verified_trading_invitees 本月认证的L1+L2+L3之和
 * @param active_trading_invitees   本月激活L1+L2+L3之和
 * @param activity_rate             本月激活率
 * @param create_time               创建时间
 * @param modify_time               修改时间
 * @param report_month              报告月份
 */
case class PalmforceTotalBean( member_id:String,
                               level:Int,
                               new_verified_1: Long,
                               new_verified_2: Long,
                               new_verified_3: Long,
                               trading_volume_1: Long,
                               trading_volume_2: Long,
                               trading_volume_3: Long,
                               base_invitees: Long,
                               verified_trading_invitees: Long,
                               active_trading_invitees: Long,
                               activity_rate: Double,
                               var create_time:Date,
                               modify_time: Date,
                               report_month: String
                             )

/**
 * 给PalmforceRole打标签
 *
 * @param member_id
 * @param first_name           推荐人id
 * @param father_name          推荐人的first_name
 * @param verified_trade      认证交易状态 0：没有  1：有
 * @param verified_trade_time 认证交易时间
 * @param active_trade        激活交易状态  0：没有  1 ：有
 * @param active_trade_time   激活交易时间
 * @param registered_time     该会员注册时间
 */
case class PalmforceRoleTagBean(
                                 member_id: String,
                                 first_name: String,
                                 father_name:String,
                                 verified_trade: Int,
                                 verified_trade_time: Date,
                                 active_trade: Int,
                                 active_trade_time: Date,
                                 registered_time: Date,
                                 modify_time: Date,
                                 report_month: String
                               )

/**
 *
 *计算ForceTotal的样例类
 *
 */
case class MemberDetail(member_id: String,
                        new_verified_1: Long,
                        new_verified_2: Long,
                        new_verified_3: Long,
                        total_verified_1: Long,
                        total_verified_2: Long,
                        total_verified_3: Long,
                        activity_rate: Double,
                        create_time: Date,
                        modify_time: Date, month: String)

/**
 * 计算PalmforceIncentiveMonth的样例类
 *
 * @param member_id
 * @param contributor
 * @param contributor_relative_level
 * @param currency
 * @param app_source
 * @param remark
 *
 */
case class PalmforceIncentiveMonthBean(var report_month:String,
                                        var member_id:String,
                                        var contributor:String,
                                        var contributor_relative_level:Int,
                                        var trading_volume:Long,
                                        var referral_bonus:Long,
                                        var commission:Long,
                                        var rewards:Long,
                                       var settled_rewards:Long,
                                       var unsettled_rewards:Long,
                                        var currency:String,
                                        var app_source:Int,
                                        var remark:String,
                                        var create_time:Date,
                                        var update_time:Date)
/*
*
* */
case class PalmforceIncentiveHistoryBean(var member_id:String,
                                         var contributor:String,
                                         var contributor_relative_level:Int,
                                         var trading_volume:Long,
                                         var referral_bonus:Long,
                                         var commission:Long,
                                         var rewards:Long,
                                         var settled_rewards:Long,
                                         var unsettled_rewards:Long,
                                         var currency:String,
                                         var app_source:Int,
                                         var remark:String,
                                         var create_time:Date,
                                         var update_time:Date)
/**
 * 统计是否满足加入palmforce 的要求
 *
 * @param member_id
 * @param threshold_calculation
 */
case class PalmforceBean(member_id: String,
                         threshold_calculation: Long,
                         var create_time:Date,
                         update_time:Date)

