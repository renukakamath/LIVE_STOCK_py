from flask import*
from database import*


api=Blueprint('api',__name__)

@api.route('/Login')
def Login():
	data={}
	uname=request.args['username']
	pwd=request.args['password']
	q="SELECT * FROM `login` WHERE `username`='%s' AND `password`='%s'"%(uname,pwd)
	res=select(q)
	print(q)
	print(res)

	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	return str(data)

@api.route('/Registration')	
def Registration():
	data={}
	f=request.args['firstname']
	l=request.args['lastname']
	p=request.args['phone']
	e=request.args['email']
	lo=request.args['longitude']
	la=request.args['latitude']
	u=request.args['username']
	pa=request.args['password']
	q="insert into login values(null,'%s','%s','customer')"%(u,pa)
	id=insert(q)
	q="insert into customer values(null,'%s','%s','%s','%s','%s','%s','%s')"%(id,f,l,p,e,lo,la)
	ids=insert(q)
	if ids:
		data['status']="success"
		data['data']=ids
	else:
		data['status']='failed'
	return str(data)

@api.route('/viewfeed')
def viewfeed():
	data={}
	q="select * from feed_products";
	res=select(q)
	data['data']=res
	data['status']="success"
	return str(data)
	
	
@api.route('/purchase')
def purchase():
	data={}
	a=request.args['amount']
	qa=request.args['quantity']
	lid=request.args['lid']
	fid=request.args['fid']
	t=request.args['total']

	q="select * from purchase_master where customer_id=(select customer_id from customer where login_id='%s') and status='pending'"%(lid)
	res=select(q)
	if res:
		pid=res[0]['pmaster_id']
	else:

		q="insert into purchase_master values(null,(select customer_id from customer where login_id='%s'),'0',now(),'pending')"%(lid)
		pid=insert(q)

	q="select * from purchase_details where feed_id='%s' and pmaster_id='%s'"%(fid,pid)
	res=select(q)
	if res:
		pdid=res[0]['pdetails_id']
		q="update purchase_details set quantity=quantity+'%s' , amount=amount+'%s' where pdetails_id='%s'"%(qa,a,pdid)
		update(q)
	else:
		q1="insert into purchase_details values(null,'%s','%s','%s','%s','0')"%(pid,fid,a,qa)
		insert(q1)

	q="update purchase_master set total=total+'%s' where pmaster_id='%s'"%(t,pid)	
	update(q)
	q="update feed_products set quantity=quantity-'%s' where feed_id='%s'"%(qa,fid)
	update(q)
	data['status']="success"
	return str(data)

@api.route('/viewpurchase')	
def viewpurchase():
	data={}
	lid=request.args['lid']
	q="select * from purchase_details inner join purchase_master using (pmaster_id) inner join feed_products using (feed_id) where customer_id=(select customer_id from customer where login_id='%s') "%(lid)
	res=select(q)
	data['data']=res
	data['status']="success"
	data['method']="viewpurchase"
	return str(data)

@api.route('/Makepayment')
def Makepayment():
	data={}
	pid=request.args['pid']
	t=request.args['amount']
	d=request.args['date']
	pdid=request.args['pdetails_id']
	q="insert into `payment` values(null,'%s','%s',curdate())"%(pid,t)
	insert(q)
	q="update purchase_master set status='payed' where pmaster_id='%s'"%(pid)
	update(q)
	q="update purchase_details set date='%s' where pdetails_id='%s' "%(d,pdid)
	update(q)


	print(q)
	data['status']="success"
	return str(data)

@api.route('/complaint')	
def complaint():
	data={}
	lid=request.args['lid']
	c=request.args['complaint']
	q="insert into complaint values(null,(select customer_id from customer where login_id='%s'),'%s','pending',curdate())"%(lid,c)
	insert(q)
	data['status']="success"
	data['method']="complaint"
	return str(data)

@api.route('/viewcomplaints')
def viewcomplaints():
	data={}
	lid=request.args['lid']
	q="select * from complaint inner join customer using (customer_id) where customer_id=(select customer_id from customer where login_id='%s') "%(lid)
	res=select(q)
	data['data']=res
	data['status']="success"
	data['method']="viewcomplaints"
	return str(data)

@api.route('/conformproduct')	
def conformproduct():
	data={}
	pid=request.args['pid']
	q="update purchase_master set status='conform' where pmaster_id='%s' "%(pid)
	update(q)
	data['status']="success"
	data['method']="conformproduct"
	return str(data)

@api.route('/ViewProduct')	
def ViewProduct():
	data={}
	lid=request.args['lid']
	from datetime import date
	today = date.today()
	print("Today date is: ", today)
	q="select * from purchase_details inner join purchase_master using (pmaster_id) inner join feed_products using (feed_id) where customer_id=(select customer_id from customer where login_id='%s') and purchase_details.date<='%s'"%(lid,today)
	print(q)
	res=select(q)
	if res:
		data['status']="Expired"
		data['method']="ViewProduct"
	return str(data)

@api.route('/Viewshortage')	
def Viewshortage():
	data={}

	lid=request.args['lid']
	q="select * from shortage_request inner join supplier using (supplier_id) inner join feed_products using (feed_id) where login_id='%s'"%(lid)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
		return str(data)

@api.route('/Accept')	
def Accept():
	data={}
	rid=request.args['rid']
	q="update shortage_request set status='Accept' where request_id='%s'"%(rid)
	update(q)
	data['status']="success"
	
	return str(data)
@api.route('/Reject')
def Reject():
	data={}
	rid=request.args['rid']
	q="update shortage_request set status='Reject' where request_id='%s'"%(rid)	
	update(q)
	data['status']="success"
	return str(data)

@api.route('/Refillproduct')	
def Refillproduct():
	data={}
	fid=request.args['fid']
	rid=request.args['rid']
	p=request.args['product']
	q="update feed_products set quantity=quantity+'%s' where feed_id='%s'"%(p,fid)
	update(q)
	q="update shortage_request set status='Filled' where request_id='%s'"%(rid)
	update(q)
	data['status']="success"
	return str(data)

@api.route('/CashOndelivery')
def CashOndelivery():
	data={}
	pid=request.args['pid']
	q="update purchase_master set status='cash On delivery'where pmaster_id='%s'"%(pid)
	update(q)
	data['status']="success"
	return str(data)

@api.route('/customerdetails')
def customerdetails():
	data={}
	lid=request.args['lid']
	q="select * from customer where  login_id='%s'"%(lid)
	res=select(q)
	data['data']=res[0]['phone']
	data['status']="success"
	data['method']="customerdetails"
	return str(data)

		
		


	




			
		




	



			