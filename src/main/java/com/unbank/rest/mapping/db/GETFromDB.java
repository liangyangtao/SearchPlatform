package com.unbank.rest.mapping.db;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

@Component
@Path("/mapping")
public class GETFromDB {
	@Path("/mappingDB")
	@GET
	public String mappingDB() {
		return "ok";
	}
}
