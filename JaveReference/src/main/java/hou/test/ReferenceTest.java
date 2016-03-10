package hou.test;

/**
 * @author houweitao
 * @date 2015年11月20日 下午6:28:18
 */

public class ReferenceTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReferenceTest rt = new ReferenceTest();
//		String s = "not change";
//		System.out.println(s);
//		rt.changeString(s);
//		System.out.println(s);

		int[] nums = { 5, 6, 7, 8 };
		rt.changeArray(nums);
		for (int i = 0; i < nums.length; i++)
			System.out.print(nums[i] + ",");
		System.out.println();

		myStructure my = new myStructure(2, "not change");
		System.out.println("1:" + my);
		rt.changeStructure(my);
		my.toString();
		System.out.println("2:" + my);
	}

	void changeString(String s) {
		s = "change";
		System.out.println(s);
	}

	// 数组的话还没明白是什么意思。
	// 为什么修改nums[2]=0之后，源数据依然没有改变呢？
	void changeArray(int[] nums) {

		/* 这里如果注释掉的话，数组的值就改变了。为什么？ */
		int[] after = { 1, 2, 3, 4 };
		System.out.println(nums);
		System.out.println(after);
		// 这里应该是对nums所携带的地址的数值进行了修改，如此之后，所有的操作都是在修改after？
		nums = after;
		System.out.println(nums);

		for (int i = 0; i < nums.length; i++)
			System.out.print(nums[i] + ",");
		System.out.println();
		/* 分割线 */

		nums[2] = 0;

		System.out.println(nums);

		for (int i = 0; i < nums.length; i++)
//			System.out.print(after[i] + ",,");//确实是修改了after！！
			System.out.print(nums[i] + ",,");
		System.out.println();
	}

	// 这里有说服性。如果只是对函数名进行赋值，那么源数据不会改变。如果改变了属性。则改变。
	// my携带的只是传入的对象的地址的“数值”，只是一个复制的简单数字而已。
	// 这里JVM应该进行了处理，如果直接对my赋值，那么my的值的改变，对源数据无影响
	// 如果对my.age进行赋值，那么直接是对对象本身赋值了。JVM会根据地址去找到源数据，修改数据
	// 这样子源数据就改变了。这里如果基本类型String有某个属性，比如长度可以赋值的话，去修改长度，应该也可以起到作用
	void changeStructure(myStructure my) {
		my.age = 1;
		my.name = "change";

//		myStructure you=new myStructure(1,"change");
//		my=you;
	}
}

class myStructure {
	int age;
	String name;

	myStructure(int age, String name) {
		this.age = age;
		this.name = name;
	}

	@Override
	public String toString() {
		System.out.println(age + name);
		return age + name + "";
	}
}