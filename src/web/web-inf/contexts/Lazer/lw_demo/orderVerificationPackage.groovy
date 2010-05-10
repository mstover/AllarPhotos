import com.allarphoto.ajaxclient.client.beans.*

pack = new OrderVerificationPackage("","On-Line Library")

pack.addField(new SelectField(name:"Intended Use",required:true,values:["","Advertising","Brochure","Newsletter","Public Relations","Web","Other"]))

return pack