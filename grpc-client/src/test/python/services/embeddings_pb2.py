# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: embeddings.proto
# Protobuf Python Version: 4.25.1
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x10\x65mbeddings.proto\"\x1c\n\x0cTrainRequest\x12\x0c\n\x04\x64\x61ta\x18\x01 \x01(\t\" \n\rTrainResponse\x12\x0f\n\x07message\x18\x01 \x01(\t\"\x1c\n\x0cHelloRequest\x12\x0c\n\x04name\x18\x01 \x01(\t\"\x1d\n\nHelloReply\x12\x0f\n\x07message\x18\x01 \x01(\t2G\n\x11\x45mbeddingsService\x12\x32\n\x0fTrainEmbeddings\x12\r.TrainRequest\x1a\x0e.TrainResponse\"\x00\x32\x33\n\x07Greeter\x12(\n\x08SayHello\x12\r.HelloRequest\x1a\x0b.HelloReply\"\x00\x62\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'embeddings_pb2', _globals)
if _descriptor._USE_C_DESCRIPTORS == False:
  DESCRIPTOR._options = None
  _globals['_TRAINREQUEST']._serialized_start=20
  _globals['_TRAINREQUEST']._serialized_end=48
  _globals['_TRAINRESPONSE']._serialized_start=50
  _globals['_TRAINRESPONSE']._serialized_end=82
  _globals['_HELLOREQUEST']._serialized_start=84
  _globals['_HELLOREQUEST']._serialized_end=112
  _globals['_HELLOREPLY']._serialized_start=114
  _globals['_HELLOREPLY']._serialized_end=143
  _globals['_EMBEDDINGSSERVICE']._serialized_start=145
  _globals['_EMBEDDINGSSERVICE']._serialized_end=216
  _globals['_GREETER']._serialized_start=218
  _globals['_GREETER']._serialized_end=269
# @@protoc_insertion_point(module_scope)
