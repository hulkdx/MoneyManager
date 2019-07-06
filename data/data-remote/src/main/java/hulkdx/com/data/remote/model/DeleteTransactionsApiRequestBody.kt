package hulkdx.com.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/07/2019.
 *
 * TODO: remove this class
 */
class DeleteTransactionsApiRequestBody(
        @field:SerializedName("id")
        @field:Expose
        val id: LongArray
)