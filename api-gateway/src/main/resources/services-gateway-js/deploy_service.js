/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/** @module services-gateway-js/deploy_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JDeployService = io.github.pflima92.plyshare.gateway.services.DeployService;

/**
 @class
*/
var DeployService = function(j_val) {

  var j_deployService = j_val;
  var that = this;

  /**

   @public
   @param metadata {Object} 
   @param resultHandler {function} 
   @return {DeployService}
   */
  this.deploy = function(metadata, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_deployService["deploy(io.vertx.core.json.JsonObject,io.vertx.core.Handler)"](utils.convParamJsonObject(metadata), function(ar) {
      if (ar.succeeded()) {
        resultHandler(ar.result(), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param metadata {Object} 
   @param resultHandler {function} 
   @return {DeployService}
   */
  this.stop = function(metadata, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_deployService["stop(io.vertx.core.json.JsonObject,io.vertx.core.Handler)"](utils.convParamJsonObject(metadata), function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_deployService;
};

// We export the Constructor function
module.exports = DeployService;