#!/usr/bin/env python
from threading import Lock
from flask_socketio import SocketIO, emit, join_room, leave_room, namespace, close_room, rooms, disconnect
from flask import Flask, session, redirect, url_for, escape, request, render_template,jsonify
import datetime
import json
from pymongo import MongoClient
from flask_cors import CORS

client = MongoClient("mongodb://localhost:27017/", w=0,socketTimeoutMS=90000)
db = client.mockdb

async_mode = None

app = Flask(__name__)
cors = CORS(app, resources={r"/api/*": {"origins": "*"}})
app.secret_key= "secerets"

socketio = SocketIO(app, async_mode=async_mode)
thread = None
thread_lock = Lock()
room_count={}


@app.after_request
def after_request(response):
    response.headers.add('Access-Control-Allow-Origin', '*')
    response.headers.add('Access-Control-Allow-Headers', 'Content-Type,Authorization')
    response.headers.add('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE')
    return response

@app.route('/')
def index():
    return render_template("index.html")
    

@app.route("/in", methods=['POST'])
def signin():
    #req=request.get_json()
    #username= req['username']
    #session['username']= username
    #return jsonify({'status':"OK", "username":session['username']})
    return jsonify({'status':"OK"})

@app.route("/out", methods=['POST'])
def signout():
   
    session.pop('username', None)

    return jsonify({'status':"OK" })

@app.route ("/sync",methods=['GET'])
def sync():
    return render_template('sync.html', async_mode=socketio.async_mode)

@app.route ("/sync",methods=['POST'])
#json:
# message:{room:session.username, data:<text>}
def sync_post():
    req=request.get_json()
    message= req['message']
    #message['room'] = session['username']
    date=datetime.datetime.now()

    socketio.emit('my_response',
                        { 'data': str(date)+" "+message['data']}, 
                        room=session['username']
                        #namespace='/sync'
    )
    return jsonify({'status':"OK"})
@app.route ("/sync/n",methods=['POST'])
#json:
# message:{room:session.username, data:<text>}
def sync_post_n():
    req=request.get_json()
    message= req['message']
    user= req['user']
    date=datetime.datetime.now()

    socketio.emit('my_response',
                        { 'data': str(date)+" "+message['data']}, 
                        room=user
                    
    )
    return jsonify({'status':"OK"})

@app.route("/hi")
def hi():
    return "hi"+session['username']+'<br>' +'<iframe src= "/hi"></iframe>'

@app.route("/assignments/", methods=['GET'])
def assignlist():
    assignments=db.assignments.find({}, {"id":1, "name":1, "_id":0})
    alist=[]
    for assignment in assignments:
        if assignment is None:
            break
        a={"id":int(assignment['id']),"name":assignment['name']}
        alist.append(a)
        #alist[int(assignment['id'])]=assignment['name']
    return render_template('assignments.html',data=alist)

@app.route("/assignments/", methods=['POST'])
def postlist():
    assignments=db.assignments.find({}, {"id":1, "name":1, "_id":0})
    alist=[]
    for assignment in assignments:
        a={"id":int(assignment['id']),"name":assignment['name']}
        alist.append(a)
        #alist[int(assignment['id'])]=assignment['name']
    return jsonify(alist)

@app.route("/assignments/<id>", methods=['GET'])
def assign_details(id):
    id= int(id)
    assignment=db.assignments.find_one({"id":id}, {"_id":0})
    if(assignment == None):
        assignment ={}
    assignment['id'] = id
    return render_template('assignment_details.html',data=assignment)

@app.route("/assignments/<id>", methods=['POST'])
def post_details(id):
    id= int(id)
    assignment=db.assignments.find_one({"id":id}, {"_id":0})
    assignment['id'] = int(assignment['id'])
    return jsonify(assignment)

@app.route("/assignments/<id>/sync", methods=['POST'])
def sync_assign(id):
    #req=request.get_json()
    message={}
    message['data']= id
    #I dont actually need all the assignment details here...
    message['room'] = session['username']

    socketio.emit('assignment',
                        {'data': message['data']}, 
                        room=session['username'],
                        #namespace='/sync'
    )
    return jsonify({'status':"OK"})
@app.route("/assignments/<id>/sync/M", methods=['POST'])
def syncM_assign(id):
    #req=request.get_json()
    message={}
    message['data']= id
    #I dont actually need all the assignment details here...
    message['room'] = session['username']

    socketio.emit('Massignment',
                        {'data': message['data']}, 
                        room=session['username'],
                        #namespace='/sync'
    )
    return jsonify({'status':"OK"})
@app.route("/assignments/<id>/sync/W", methods=['POST'])
def syncW_assign(id):
    #req=request.get_json()
    message={}
    message['data']= id
    #I dont actually need all the assignment details here...
    message['room'] = session['username']

    socketio.emit('Wassignment',
                        {'data': message['data']}, 
                        room=session['username'],
                        #namespace='/sync'
    )
    return jsonify({'status':"OK"})
@app.route("/assignments/<id>/sync/M/n", methods=['POST'])
def syncMn_assign(id):
    req=request.get_json()
    message={}
    message['data']= id
    user= req['user']

    socketio.emit('Massignment',
                        {'data': message['data']}, 
                        room=user,
                        #namespace='/sync'
    )
    return jsonify({'status':"OK"})
@app.route("/assignments/<id>/sync/W/n", methods=['POST'])
def syncWn_assign(id):
    req=request.get_json()
    message={}
    message['data']= id
    user= req['user']

    socketio.emit('Wassignment',
                        {'data': message['data']}, 
                        room=user,
                        #namespace='/sync'
    )
    return jsonify({'status':"OK"})

@app.route("/time", methods=['GET','POST'])
def time():
    date=datetime.datetime.now()
    return str(date)
@app.route("/test", methods=['POST','GET'])
def test():
    return jsonify({"status":"OK","hello":"hi"})   
@app.route("/echo", methods=['GET','POST'])
def echo():
    return jsonify(request.get_json())
@app.route("/who", methods=['GET','POST'])
def whoami():
    return session.get('username', 'not set')
    
@app.route("/blah")
def blah():
    return socketio.get_details
# def background_thread():
#     """Example of how to send server generated events to clients."""
#     count = 0
#     while True:

#         socketio.sleep(10)
#         count += 1
#         date=datetime.datetime.now()
#         socketio.emit('my_response',
#                       {'data': 'Server generated event'+str(date), 'count': count},
#                       namespace='/sync')

@socketio.on('my_event')# namespace='/sync')
def test_message(message):
    date=datetime.datetime.now()
    session['receive_count'] = session.get('receive_count', 0) + 1
    emit('my_response',
         {'data': str(date)+" "+message['data'], 'count': session['receive_count']})


@socketio.on('my_broadcast_event')#, namespace='/sync')
def test_broadcast_message(message):
    date=datetime.datetime.now()
    session['receive_count'] = session.get('receive_count', 0) + 1
    emit('my_response',
         {'data': str(date)+" "+message['data'], 'count': session['receive_count']},
         broadcast=True)


@socketio.on('join')#, namespace='/sync')
def join(message):
    join_room(message['room'])
    date=datetime.datetime.now()
  #  session['receive_count'] = session.get('receive_count', 0) + 1
    #r=rooms()[''][message['room']]
    room_count[message['room']]= room_count.get(message['room'],0)+1
    emit('my_response',
         {'data': str(date)+"  new connection to "+message['room']+"\n currently connected :"+ str(room_count[message['room']])},
         room=message['room'])

@socketio.on('leave')#, namespace='/sync')
def leave(message):
    leave_room(message['room'])
    room_count[message['room']]= room_count[message['room']]-1

    emit('my_response',
         {'data': "someone left room, currently: "+ str(room_count[message['room']])},
         room=message['room'])



@socketio.on('close_room')#, namespace='/sync')
def close(message):
    session['receive_count'] = session.get('receive_count', 0) + 1
    emit('my_response', {'data': 'Room ' + message['room'] + ' is closing.',
                         'count': session['receive_count']},
         room=message['room'])
    close_room(message['room'])



@socketio.on('my_room_event')#, namespace='/sync')
def send_room_message(message):
    date=datetime.datetime.now()
    session['receive_count'] = session.get('receive_count', 0) + 1
    emit('my_response',
         {'data': str(date)+" "+message['data'], 'count': session['receive_count']},
         room=message['room'], include_self= False)
    emit('my_response',
         {'data': str(date)+" SENT", })


@socketio.on('disconnect_request')#, namespace='/sync')
def disconnect_request():
    session['receive_count'] = session.get('receive_count', 0) + 1

    emit('my_response',
         {'data': 'Disconnected!', 'count': session['receive_count']})
    disconnect()


@socketio.on('my_ping')#, namespace='/sync')
def ping_pong():
    emit('my_pong')


@socketio.on('connect')#, namespace='/sync')
def test_connect():
    #global thread
    #with thread_lock:
    #    if thread is None:
   #         thread = socketio.start_background_task(target=background_thread)
    emit('my_response', {'data': 'Connected', 'count': 0})

@socketio.on('disconnect')#, namespace='/sync')
def test_disconnect():
 #   emit('my_response',
  #       {'data': "disconnected"},
  #       broadcast=True)
    print('Client disconnected', request.sid)


if __name__ ==     "__main__":
    app.run(host='0.0.0.0', port = 80, debug=True)
