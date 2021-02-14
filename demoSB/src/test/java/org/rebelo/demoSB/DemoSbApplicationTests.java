package org.rebelo.demoSB;

import org.junit.jupiter.api.Test;
import org.rebelo.demoSB.controladores.ControladorVeiculoApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DemoSbApplicationTests {

	@Autowired
	private ControladorVeiculoApi controller;

	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
	}

}