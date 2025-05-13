//package com.example.User;
//
//import com.example.User.Model.User;
//import com.example.User.Repository.UserRepository;
//import com.example.User.Service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith(MockitoExtension.class) // JUnit 5 extension for Mockito
//class UserApplicationTests {
//
//	@Mock
//	private UserRepository userRepository;
//
//	@InjectMocks
//	private UserService userService;
//
//	@Mock
//	private BCryptPasswordEncoder passwordEncoder;
//
//	private User user1, user2, user3, user4;
//
//	@BeforeEach
//	void setUp() {
//		setUpUser();
//	}
//
//	void setUpUser() {
//		user1 = new User.UserBuilder()
//				.id(1L)
//				.username("yousefbadr")
//				.email("yousefbadr@gmail.com")
//				.password("123456")
//				.isActive(true)
//				.build();
//
//		user2 = new User.UserBuilder()
//				.username("faresbadr")
//				.email("faresbadr@gmail.com")
//				.password("123456")
//				.isActive(true)
//				.build();
//
//		user3 = new User.UserBuilder()
//				.username("fawzybadr")
//				.email("fawzybadr@gmail.com")
//				.password("123456")
//				.isActive(true)
//				.build();
//
//		user4 = new User.UserBuilder()
//				.username("yehiabadr")
//				.email("yehiabadr@gmail.com")
//				.password("123456")
//				.isActive(true)
//				.build();
//	}
//
//	@Test
//	void testAddUser() {
//		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user1);
//		Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
//
//		ResponseEntity<String> response = userService.addUser(user1);
//
//		System.out.println(user1.getId() + " hello " + user1.getPassword());
//		Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
//		assertEquals("User added successfully", response.getBody());
//		assertEquals(200, response.getStatusCodeValue());
//	}
//
//	@Test
//	void testGetUser() {
//		Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(user1));
//
//		ResponseEntity<User> response = userService.getUser(2l);
//
//		Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
//		assertEquals(user1, response.getBody());
//		assertEquals(200, response.getStatusCodeValue());
//	}
//}
