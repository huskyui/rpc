// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Message.proto

package com.huskyui.rpc.model;

public final class MessageProto {
  private MessageProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Message_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_Message_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\rMessage.proto\"{\n\007Message\022)\n\013messageTyp" +
      "e\030\001 \001(\0162\024.Message.MessageType\022\014\n\004body\030\002 " +
      "\001(\t\"7\n\013MessageType\022\016\n\nHEART_BEAT\020\000\022\n\n\006ON" +
      "LINE\020\001\022\014\n\010RESPONSE\020\002B\'\n\025com.huskyui.rpc." +
      "modelB\014MessageProtoP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_Message_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_Message_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_Message_descriptor,
        new String[] { "MessageType", "Body", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
