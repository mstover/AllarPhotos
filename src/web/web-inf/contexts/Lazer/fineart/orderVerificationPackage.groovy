import com.allarphoto.ajaxclient.client.beans.*

pack = new OrderVerificationPackage("","FineArt On-Line Library")

pack.addField(new SelectField(name:"Delivery Format",required:true,values:["","Giclee","Framed Image"]))
pack.addField(new AreaField(name:"Special Instructions",required:false))

return pack