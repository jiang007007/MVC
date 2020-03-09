package com.nike.po; 
import java.util.Date;
public class UrpUser{
	private Byte type;
	private String userName;
	private String deparment;
	private Integer userTell;
	private Integer userId;
	private Date createTime;
	private String password;
	public Byte getType(){
		return type;
	}
	public void setType(Byte type){
		this.type= type;
	}
	public String getUsername(){
		return userName;
	}
	public void setUsername(String userName){
		this.userName= userName;
	}
	public String getDeparment(){
		return deparment;
	}
	public void setDeparment(String deparment){
		this.deparment= deparment;
	}
	public Integer getUsertell(){
		return userTell;
	}
	public void setUsertell(Integer userTell){
		this.userTell= userTell;
	}
	public Integer getUserid(){
		return userId;
	}
	public void setUserid(Integer userId){
		this.userId= userId;
	}
	public Date getCreatetime(){
		return createTime;
	}
	public void setCreatetime(Date createTime){
		this.createTime= createTime;
	}
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password= password;
	}

}