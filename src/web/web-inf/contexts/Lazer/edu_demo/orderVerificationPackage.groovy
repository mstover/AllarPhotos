import com.allarphoto.ajaxclient.client.beans.*

pack = new OrderVerificationPackage("","University Image Library")

pack.addField(new SelectField(name:"Intended Use",required:true,values:["","Public Relations","Outside Vendor","Marketing Comm","Misc"]))

return pack