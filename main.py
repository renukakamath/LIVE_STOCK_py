from flask import Flask 
from public import public
from admin import admin
from api import api

app=Flask(__name__)
app.secret_key="key"

app.register_blueprint(public)
app.register_blueprint(admin,url_prefix='/admin')
app.register_blueprint(api,url_prefix='/api')


app.run(debug=True, host="0.0.0.0",port=5055)