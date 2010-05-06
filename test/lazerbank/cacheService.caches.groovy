import strategiclibrary.service.cache.*
import com.lazerinc.ecommerce.*
import com.lazerinc.server.product.*
import com.lazerinc.server.ugd.*
import com.lazerinc.cached.functions.*
import com.lazerinc.utils.*
import org.coinjema.util.*
import com.lazerinc.beans.*
import com.lazerinc.client.ServiceGateway
import strategiclibrary.util.*

pre = new Functor("getProductFamilyName").postChain(ServiceGateway.getProductService(),"getProductFamily")
[new CacheRegistration(cacheTime : TimeConstants.DAY,
           objectType : CommerceProduct.class,
           mainRetrieval : new Functor(ServiceGateway.getProductService(),"getAllProducts"),
           categoryFunctors : [ "Primary":new Functor("getPrimary"),
                                "id":new Functor("getId") ],
           processFunctors : [new Functor("setProductFamily").preChain(pre)],
           organizingCategory : "family",
           organizingFunctor : new Functor("getProductFamilyName"),
           primaryPath : ["family",null,"id",null] as Object[],
           primaryFunctors : [new Functor("getProductFamilyName"),new Functor("getId")],
           adHocRetrieval : new Functor(ServiceGateway.getProductService(),"findProducts")),
           
 new CacheRegistration(cacheTime : TimeConstants.HOUR * 4,
 			objectType : new CommerceUser().class,
           retrievalSql : "findUsers.sql",
           insertSql : "addUser.sql",
           deleteSql : "deleteUser.sql",
           updateSql : "updateUser.sql",
           updateFunctor : new Functor(new UserAdd(),"update"),
           addFunctor : new Functor(new UserAdd(),"add"),
           categoryFunctors : ["username" : new Functor("getUsername"),
                               "id" : new Functor("getUserID")],
           primaryPath : ["id",null],
           primaryFunctors : [new Functor("getUserID")]),
           
 new CacheRegistration(cacheTime : TimeConstants.HOUR * 4,
 			objectType : new UserGroup().class,
           retrievalSql : "findGroups.sql",
           insertSql : "addGroup.sql",
           deleteSql : "deleteGroup.sql",
           updateSql : "updateGroup.sql",
           categoryFunctors : ["name" : new Functor("getName"),
                               "id" : new Functor("getId")],
           primaryPath : ["id",null],
           primaryFunctors : [new Functor("getId")]),
           
new CacheRegistration(cacheTime : TimeConstants.DAY,
 			objectType : new Resource().class,
           retrievalSql : "getResources.sql",
           insertSql : "addResource.sql",
 			addFunctor : new Functor(new ResourceAdd(),"add"),
           updateSql : "updateResource.sql",
           deleteSql : "deleteResource.sql",
           categoryFunctors : ["name" : new Functor("getName"),
                               "type":new Functor("getType"),
                               "id":new Functor("getId"),
                               ["name","type"]:null],
           primaryPath : ["id",null] as Object[],
           primaryFunctors : [new Functor("getId")]),
           
new CacheRegistration(cacheTime : TimeConstants.DAY,
 			objectType : new State().class,
           retrievalSql : "getStates.sql",
           insertSql : "addState.sql",
 			addFunctor : new Functor(new StateAdd(),"add"),
           categoryFunctors : ["name" : new Functor("getName"),
                               "code":new Functor("getCode"),
                               "id":new Functor("getId")],
           primaryPath : ["id",null] as Object[],
           primaryFunctors : [new Functor("getId")]),
           
new CacheRegistration(cacheTime : TimeConstants.DAY,
 			objectType : new Country().class,
           retrievalSql : "getCountries.sql",
           insertSql : "addCountry.sql",
 			addFunctor : new Functor(new CountryAdd(),"add"),
           categoryFunctors : ["name" : new Functor("getName"),
                               "code":new Functor("getCode"),
                               "id":new Functor("getId")],
           primaryPath : ["id",null] as Object[],
           primaryFunctors : [new Functor("getId")]),
           
new CacheRegistration(cacheTime : TimeConstants.DAY,
 			objectType : new City().class,
           retrievalSql : "getCities.sql",
           insertSql : "addCity.sql",
 			addFunctor : new Functor(new CityAdd(),"add"),
           categoryFunctors : ["name" : new Functor("getName"),
                               "id":new Functor("getId")],
           primaryPath : ["id",null] as Object[],
           primaryFunctors : [new Functor("getId")]),
           
new CacheRegistration(cacheTime : TimeConstants.DAY,
 			objectType : new Company().class,
           retrievalSql : "getCompanies.sql",
           insertSql : "addCompany.sql",
 			addFunctor : new Functor(new CompanyAdd(),"add"),
           categoryFunctors : ["name" : new Functor("getName"),
                               "id":new Functor("getId")],
           primaryPath : ["id",null] as Object[],
           primaryFunctors : [new Functor("getId")]),
           
new CacheRegistration(cacheTime : TimeConstants.DAY,
 			objectType : new Industry().class,
 			addFunctor : new Functor(new IndustryAdd(),"add"),
 			insertSql : "addIndustry.sql",
           retrievalSql : "getIndustries.sql",
           categoryFunctors : ["name" : new Functor("getName"),
                               "id":new Functor("getId")],
           primaryPath : ["id",null] as Object[],
           primaryFunctors : [new Functor("getId")]),
           
new CacheRegistration(cacheTime : TimeConstants.DAY,
 			objectType : new Referrer().class,
           retrievalSql : "getReferrers.sql",
           insertSql : "addReferrer.sql",
 			addFunctor : new Functor(new ReferrerAdd(),"add"),
           categoryFunctors : ["name" : new Functor("getName"),
                               "id":new Functor("getId")],
           primaryPath : ["id",null] as Object[],
           primaryFunctors : [new Functor("getId")]),
           
new CacheRegistration(cacheTime : TimeConstants.DAY,
 			objectType : new Address().class,
           retrievalSql : "getAddress.sql",
           insertSql : "addAddress.sql",
 			addFunctor : new Functor(new AddressAdd(),"add"),
           categoryFunctors : ["address1" : new Functor("getAddress1"),
                               "id":new Functor("getId")],
           primaryPath : ["id",null] as Object[],
           primaryFunctors : [new Functor("getId")])]