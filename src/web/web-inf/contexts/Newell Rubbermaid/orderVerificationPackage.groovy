import com.lazerinc.ajaxclient.client.beans.*

pack = new OrderVerificationPackage("","Newell Rubbermaid On-Line Library")

pack.addField(new SelectField(name:"Delivery Format",required:true,values:["","CD by Mail","FTP Upload"]))
pack.addField(new FormField(name:"FTP Details (if appropriate)",required:false))
pack.addField(new FormField(name:"Phone",required:true))
pack.addField(new AreaField(name:"Special Instructions",required:false))

return pack