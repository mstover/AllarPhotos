import com.lazerinc.ajaxclient.client.beans.*

pack = new OrderVerificationPackage("","University Image Library")

pack.addField(new SelectField(name:"Intended Use",required:true,values:["","Public Relations","Outside Vendor","Marketing Comm","Misc"]))
pack.addField(new FormField(name:"Contact Name",required:true))
pack.addField(new AreaField(name:"Notes",required:false))
return pack