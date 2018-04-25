package com.max.app.kotlincrm.items

import java.io.Serializable

/**
 * 跟进
 * Created by Xmg on 2018-4-24.
 */
class FollowUpModel : Serializable {
    var customerId: String = ""
    var followUpId: String = ""
    var enterpriseName: String = ""
    var isMineLib: Boolean = false
    var isFillInfo: Boolean = false
    var address: String = ""
    var startDate: String = ""
    var endDate: String = ""
    var contactName: String = ""
    var contactId: String = ""
    var content: String = ""
    var category: Int = 0
    var label: String = ""
}