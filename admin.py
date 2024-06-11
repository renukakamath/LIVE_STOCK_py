from public import *
import uuid

admin=Blueprint('admin',__name__)

@admin.route('/admin_home')
def admin_home():
	return render_template('admin_home.html')

@admin.route('/admin_manage_cattle_feed',methods=['get','post'])
def admin_manage_cattle_feed():
	data={}
	if 'manage' in request.form:
		name=request.form['name']
		price=request.form['price']
		qty=request.form['qty']
		img=request.files['img']
		
		path='static/'+str(uuid.uuid4())+img.filename
		img.save(path)
		q="select * from feed_products where f_name='%s'"%(name)
		res=select(q)
		if res:
			flash('THIS PRODUCT IS ALREADY EXIST')
		else:
			q="insert into feed_products values(NULL,'%s','%s','%s','%s',curdate())"%(name,price,qty,path)
			insert(q)
			print(q)
			# flash("Added Successfully...!")
		return redirect(url_for('admin.admin_manage_cattle_feed'))

	q="select * from feed_products"
	res=select(q)
	if res:
		data['don']=res
		print(res)

	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']
	else:
		action=None

	if action=='delete':
		q="delete from feed_products where feed_id='%s'"%(id)
		delete(q)
		flash("deleted.....!")
		return redirect(url_for('admin.admin_manage_cattle_feed'))

	if action=='update':
		q="select * from feed_products where feed_id='%s'"%(id)
		data['dir']=select(q)

	if 'update' in request.form:
		name=request.form['name']
		price=request.form['price']
		qty=request.form['qty']
		q="update feed_products set f_name='%s',price='%s',quantity='%s' where feed_id='%s'"%(name,price,qty,id)
		update(q)
		flash("updated")
		return redirect(url_for('admin.admin_manage_cattle_feed'))
	return render_template("admin_manage_cattle_feed.html",data=data)




@admin.route('/admin_manage_supplier',methods=['get','post'])
def admin_manage_supplier():
	data={}
	if 'submit' in request.form:
		fn=request.form['fn']
		ln=request.form['ln']
		pl=request.form['pl']
		ph=request.form['ph']
		em=request.form['em']
		uname=request.form['uname']
		pwd=request.form['pwd']
		q="select * from login where username='%s' and password='%s'"%(uname,pwd)
		res=select(q)
		if res:
			flash('THIS PRODUCT IS ALREADY EXIST')
		else:
			q="INSERT INTO `login` VALUES(null,'%s','%s','supplier')"%(uname,pwd)
			id=insert(q)
			q1="INSERT INTO `supplier` VALUES(null,'%s','%s','%s','%s','%s','%s')"%(id,fn,ln,pl,ph,em)
			insert(q1)
			flash('registered')
		return redirect(url_for('admin.admin_manage_supplier'))

	q="select * from supplier"
	res=select(q)
	if res:
		data['don']=res
		print(res)

	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']
	else:
		action=None

	if action=='delete':
		q="delete from supplier where login_id='%s'"%(id)
		delete(q)
		flash("deleted.....!")
		return redirect(url_for('admin.admin_manage_supplier'))

	if action=='update':
		q="select * from supplier where login_id='%s'"%(id)
		data['dir']=select(q)

	if 'update' in request.form:
		fn=request.form['fn']
		ln=request.form['ln']
		pl=request.form['pl']
		ph=request.form['ph']
		em=request.form['em']
		q="update supplier set fname='%s',lname='%s',place='%s',phone='%s',email='%s' where login_id='%s'"%(fn,ln,pl,ph,em,id)
		update(q)
		flash("updated")
		return redirect(url_for('admin.admin_manage_supplier'))
	return render_template("admin_manage_supplier.html",data=data)


@admin.route('/admin_view_complaints',methods=['get','post'])
def admin_view_complaints():
	data={}
	q="SELECT *,CONCAT(`fname`,' ',`lname`) AS `name` FROM `complaint` INNER JOIN `customer` USING(`customer_id`)"
	res=select(q)
	data['complaints']=res

	j=0
	for i in range(1,len(res)+1):
		print('submit'+str(i))
		if 'submit'+str(i) in request.form:
			reply=request.form['reply'+str(i)]
			print(reply)
			print(j)
			print(res[j]['complaint_id'])
			q="update complaint set reply='%s' where complaint_id='%s'" %(reply,res[j]['complaint_id'])
			print(q)
			update(q)
			flash("success")
			return redirect(url_for('admin.admin_view_complaints')) 	
		j=j+1
	return render_template('admin_view_complaints.html',data=data)



@admin.route('/admin_send_shortage_request',methods=['get','post'])
def admin_send_shortage_request():
	data={}
	q="select * from supplier"
	res=select(q)
	data['supplier']=res

	q="select * from feed_products"
	res=select(q)
	data['pro']=res

	if 'submit' in request.form:
		supplier=request.form['supplier']
		pro=request.form['pro']
		q="insert into shortage_request values(NULL,'%s','%s',curdate(),'pending')"%(supplier,pro)
		insert(q)
		flash("Request Sended...!")
		return redirect(url_for('admin.admin_send_shortage_request'))

	q="select * from shortage_request inner join supplier using(supplier_id) inner join feed_products using(feed_id)"
	res=select(q)
	data['don']=res


	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']
	else:
		action=None

	if action=='delete':
		q="delete from shortage_request where request_id='%s'"%(id)
		delete(q)
		flash("deleted.....!")
		return redirect(url_for('admin.admin_send_shortage_request'))
	return render_template('admin_send_shortage_request.html',data=data)



@admin.route('/admin_view_orders')
def admin_view_orders():
	data={}
	q="SELECT *,CONCAT(`fname`,' ',`lname`) AS `name` FROM `customer` INNER JOIN `purchase_master` USING(`customer_id`)"
	res=select(q)
	data['order']=res
	return render_template('admin_view_orders.html',data=data)


@admin.route('/admin_view_order_details')
def admin_view_order_details():
	data={}
	pm_id=request.args['pm_id']
	q="SELECT *,`purchase_details`.`quantity` AS qty FROM `purchase_details` INNER JOIN `feed_products` USING(feed_id) where pmaster_id='%s'"%(pm_id)
	res=select(q)
	data['order']=res
	return render_template('admin_view_order_details.html',data=data)