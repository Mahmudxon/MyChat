package uz.mahmudxon.mychat.model

data class User (val id : String, var firstname : String, var lastname : String, var phone : String, var imgUrl : String)
{
    companion object {
        var users = ArrayList<User>()
        fun isExist(uid : String) : Boolean
        {
            for(user in users)
                if(user.id.equals(uid))
                    return true

            return false
        }
    }
}