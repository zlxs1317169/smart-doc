/*
 * Copyright (C) 2018-2024 smart-doc
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.ly.doc.model.grpc;

import com.ly.doc.model.IDoc;
import com.ly.doc.model.IMethod;
import com.power.common.util.CollectionUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Grpc Api Doc.
 *
 * @author linwumignshi
 * @since 3.0.7
 */
public class GrpcApiDoc implements Serializable, IDoc, Comparable<GrpcApiDoc> {

	private static final long serialVersionUID = -3116322721344529338L;

	/**
	 * Order of controller
	 *
	 * @since 1.7+
	 */
	public int order;

	/**
	 * interface title
	 */
	public String title;

	/**
	 * interface name
	 */
	private String name;

	/**
	 * interface short name
	 */
	private String shortName;

	/**
	 * controller alias handled by md5
	 *
	 * @since 1.7+
	 */
	private String alias;

	/**
	 * method description
	 */
	private String desc;

	/**
	 * interface protocol
	 */
	private String protocol;

	/**
	 * interface author
	 */
	private String author;

	/**
	 * interface uri
	 */
	private String uri;

	/**
	 * interface version
	 */
	private String version;

	/**
	 * List of method doc
	 */
	private List<GrpcJavaMethod> list;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<GrpcJavaMethod> getList() {
		return list;
	}

	public void setList(List<GrpcJavaMethod> list) {
		this.list = list;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public int compareTo(GrpcApiDoc o) {
		if (Objects.nonNull(o.getDesc())) {
			return desc.compareTo(o.getDesc());
		}
		return name.compareTo(o.getName());
	}

	@Override
	public String getDocClass() {
		return this.name;
	}

	@Override
	public List<IMethod> getMethods() {
		if (CollectionUtil.isEmpty(this.list)) {
			return Collections.emptyList();
		}
		return new ArrayList<>(this.list);
	}

}
