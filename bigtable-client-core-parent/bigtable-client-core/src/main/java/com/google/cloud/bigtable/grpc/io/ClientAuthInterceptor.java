package com.google.cloud.bigtable.grpc.io;

import java.util.concurrent.Executor;

import com.google.auth.Credentials;
import com.google.common.base.Preconditions;

import io.grpc.CallCredentials;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.MethodDescriptor;
import io.grpc.auth.MoreCallCredentials;

public final class ClientAuthInterceptor implements ClientInterceptor {

  private final CallCredentials credentials;

  public ClientAuthInterceptor(
      Credentials credentials, @SuppressWarnings("unused") Executor executor) {
    this.credentials =
        MoreCallCredentials.from(
            Preconditions.checkNotNull(credentials, "credentials"));
  }

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      final MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, final Channel next) {
    return next.newCall(method, callOptions.withCallCredentials(credentials));
  }
}
