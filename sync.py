#!/usr/bin/env python
from threading import Lock
from flask_socketio import SocketIO, emit, join_room, leave_room, namespace, close_room, rooms, disconnect
from flask import Flask, session, redirect, url_for, escape, request, render_template,jsonify
import datetime
import json
from pymongo import MongoClient
from flask_cors import CORS
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

@app.route("/assignments/<id>/sync/M", methods=['POST'])
def syncMn_assign(id):
    req=request.get_json()
    message={}
    message['data']= id
    user= req['user']

    socketio.emit('Massignment',
                        {'data': message['data']}, 
                        room=user,
    )
    return jsonify({'status':"OK"})

@app.route("/assignments/<id>/sync/W", methods=['POST'])
def syncWn_assign(id):
    req=request.get_json()
    message={}
    message['data']= id
    user= req['user']

    socketio.emit('Wassignment',
                        {'data': message['data']}, 
                        room=user,                  
    )
    return jsonify({'status':"OK"})



@socketio.on('join')
def join(message):
    join_room(message['room'])

@socketio.on('my_room_event')
def send_room_message(message):
    date=datetime.datetime.now()
    session['receive_count'] = session.get('receive_count', 0) + 1
    emit('my_response',
         {'data': str(date)+" "+message['data'], 'count': session['receive_count']},
         room=message['room'], include_self= False)



@socketio.on('connect')
def test_connect():
    emit('my_response', {'data': 'Connected', 'count': 0})

@socketio.on('disconnect')
def test_disconnect():
    print('Client disconnected', request.sid)


if __name__ ==     "__main__":
    app.run(host='0.0.0.0', port = 80, debug=True)
