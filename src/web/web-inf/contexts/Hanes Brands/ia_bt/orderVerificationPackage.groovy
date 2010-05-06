import com.lazerinc.ajaxclient.client.beans.*

pack = new OrderVerificationPackage("","IA Barely There On-Line Library")

pack.addField(new FormField(name:"Account No",required:true))
pack.addField(new FormField(name:"Intended Use",required:true))

return pack