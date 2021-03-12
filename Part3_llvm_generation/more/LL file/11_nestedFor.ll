@.strR = private unnamed_addr constant [3 x i8] c"%d\00", align 1
define i32 @readInt() {
  %x = alloca i32, align 4
  %1 = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.strR, i32 0, i32 0), i32* %x)
  %2 = load i32, i32* %x, align 4
  ret i32 %2
}
declare i32 @__isoc99_scanf(i8*, ...)
@.strP = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1
define void @println(i32 %x) {
  %1 = alloca i32, align 4
  store i32 %x, i32* %1, align 4
  %2 = load i32, i32* %1, align 4
  %3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.strP, i32 0, i32 0), i32 %2)
  ret void
}
declare i32 @printf(i8*, ...)
define void @defaultFunctionName0(){
%a_0 = alloca i32
store i32 32, i32* %a_0
%b_0 = alloca i32
store i32 2, i32* %b_0
%j_0 = alloca i32
store i32 0, i32* %j_0
%1 = sub i32 458, 55
store i32 %1, i32* %j_0
br label %forCond0
forCond0:
%comp1_0 = alloca i32
%2 = add i32 100, 232
%3 = sub i32 %2, 2
store i32 %3, i32* %comp1_0
%4 = load i32, i32* %j_0
%5 = load i32, i32* %comp1_0
%6 = icmp sgt i32 %5, 0
br i1 %6, label %forward6, label %backward6
br label %forward6
forward6:
%8 = icmp sle i32 %4, %5
br i1 %8, label %forCode0, label %forOut0
backward6:
%9 = icmp sge i32 %4, %5
br i1 %9, label %forCode0, label %forOut0
br label %forCode0
forCode0:
%11 = load i32, i32* %a_0
call void @println(i32 %11)
%i_0 = alloca i32
store i32 0, i32* %i_0
%12 = load i32, i32* %j_0
store i32 %12, i32* %i_0
br label %forCond11
forCond11:
%comp12_0 = alloca i32
store i32 10, i32* %comp12_0
%13 = load i32, i32* %i_0
%14 = load i32, i32* %comp12_0
%15 = icmp sgt i32 %14, 0
br i1 %15, label %forward15, label %backward15
br label %forward15
forward15:
%17 = icmp sle i32 %13, %14
br i1 %17, label %forCode11, label %forOut11
backward15:
%18 = icmp sge i32 %13, %14
br i1 %18, label %forCode11, label %forOut11
br label %forCode11
forCode11:
%20 = load i32, i32* %b_0
call void @println(i32 %20)
%addVal20_0 = alloca i32
store i32 5, i32* %addVal20_0
%21 = load i32, i32* %addVal20_0
%22 = load i32, i32* %i_0
%23 = add i32 %22, %21
store i32 %23, i32* %i_0
br label %forCond11
forOut11:
%addVal23_0 = alloca i32
%24 = load i32, i32* %b_0
store i32 %24, i32* %addVal23_0
%25 = load i32, i32* %addVal23_0
%26 = load i32, i32* %j_0
%27 = add i32 %26, %25
store i32 %27, i32* %j_0
br label %forCond0
forOut0:
ret void
}

define i32 @main(){
call void @defaultFunctionName0()
ret i32 0
}
