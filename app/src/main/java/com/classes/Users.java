package com.classes;

public class Users {
	String Fullname,Companyname,Contactperson,Mobileno,Address1,Address2,City,Price,Retailwarecustomer,Emailid , Password , IsAdmin , IsActive , CreationDate , CreationTime , LastmodifiedDate , Lastmodifiedtime , Expiry_Date; 
	int UserID,SubscriptionID;

	public Users(int UserID,int SubscriptionID,String Fullname,String Companyname,String Contactperson,String Mobileno,String Address1,String Address2,String City,String Price,String Retailwarecustomer,String Emailid , String Password , String IsAdmin ,String IsActive ,String CreationDate ,String CreationTime ,String LastmodifiedDate ,String Lastmodifiedtime,String Expiry_Date)
	{
		this.UserID=UserID;
		this.SubscriptionID=SubscriptionID;
		this.Fullname=Fullname;
		this.Companyname=Companyname;
		this.Contactperson=Contactperson;
		this.Mobileno=Mobileno;
		this.Address1=Address1;
		this.Address2=Address2;
		this.City=City;
		this.Price=Price;
		this.Retailwarecustomer=Retailwarecustomer;
		this.Emailid=Emailid;
		this.Password=Password;
		this.IsAdmin=IsAdmin;
		this.IsActive=IsActive;
		this.CreationDate =CreationDate ;
		this.CreationTime=CreationTime;
		this.LastmodifiedDate=LastmodifiedDate;
		this.Lastmodifiedtime=Lastmodifiedtime;
		this.Expiry_Date=Expiry_Date;
	}

	public int GetUserID()
	{
		return this.UserID;
	}
	public void SetUserID(int UserID)
	{
		this.UserID=UserID;
	}
	public int GetSubscriptionID()
	{
		return this.SubscriptionID;
	}
	public void SetSubscriptionID(int SubscriptionID)
	{
		this.SubscriptionID=SubscriptionID;
	}

	public String GetFullname()
	{
		return this.Fullname;
	}
	public void SetFullname(String Fullname)
	{
		this.Fullname=Fullname;
	}

	public String GetCompanyname()
	{
		return this.Companyname;
	}
	public void SetCompanyname(String Companyname)
	{
		this.Companyname=Companyname;
	}

	public String GetContactperson()
	{
		return this.Contactperson;
	}
	public void SetContactperson(String Contactperson)
	{
		this.Contactperson=Contactperson;
	}

	public String GetMobileno()
	{
		return this.Mobileno;
	}

	public void SetMobileno(String Mobileno)
	{
		this.Mobileno=Mobileno;
	}

	public String GetAddress1()
	{
		return this.Address1;
	}
	public void SetAddress1(String Address1)
	{
		this.Address1=Address1;
	}

	public String GetAddress2()
	{
		return this.Address2;
	}
	public void SetAddress2(String Address2)
	{
		this.Address2=Address2;
	}

	public String GetCity()
	{
		return this.City;
	}
	public void SetCity(String City)
	{
		this.City=City;
	}

	public String GetPrice()
	{
		return this.Price;
	}
	public void SetPrice(String Price)
	{
		this.Price=Price;
	}

	public String GetRetailwarecustomer()
	{
		return this.Retailwarecustomer;
	}
	public void SetRetailwarecustomer(String Retailwarecustomer)
	{
		this.Retailwarecustomer=Retailwarecustomer;
	}

	public String GetEmailid()
	{
		return this.Emailid;
	}
	public void SetEmailid(String Emailid)
	{
		this.Emailid=Emailid;
	}

	public String GetPassword()
	{
		return this.Password;
	}
	public void SetPassword(String Password)
	{
		this.Password=Password;
	}

	public String GetIsAdmin()
	{
		return this.IsAdmin;
	}
	public void SetIsAdmin(String IsAdmin)
	{
		this.IsAdmin=IsAdmin;
	}

	public String GetIsActive()
	{
		return this.IsActive;
	}
	public void SetIsActive(String IsActive)
	{
		this.IsActive=IsActive;
	}

	public String GetCreationDate()
	{
		return this.CreationDate;
	}
	public void SetCreationDate(String CreationDate)
	{
		this.CreationDate=CreationDate;
	}
	
	public String GetCreationTime()
	{
		return this.CreationTime;
	}
	public void SetCreationTime(String CreationTime)
	{
		this.CreationTime=CreationTime;
	}

	public String GetLastmodifiedDate()
	{
		return this.LastmodifiedDate;
	}
	public void SetLastmodifiedDate(String LastmodifiedDate)
	{
		this.LastmodifiedDate=LastmodifiedDate;
	}

	public String GetLastmodifiedtime()
	{
		return this.Lastmodifiedtime;
	}
	public void SetLastmodifiedtime(String Lastmodifiedtime)
	{
		this.Lastmodifiedtime=Lastmodifiedtime;
	}

	public String GetExpiry_Date()
	{
		return this.Expiry_Date;
	}
	public void SetExpiry_Date(String Expiry_Date)
	{
		this.Expiry_Date=Expiry_Date;
	}
	/*public int GetID()
	{
		return this.id;
	}

	public void SetID(int id )
	{
		this.id=id;
	}
	public byte[] GetImage()
	{
		return this.image;
	}
	public void SetImage(byte [] image)
	{
		this.image=image;
	}*/
}
