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
define void @NestForWithBackwardFor(){
%a_0 = alloca i32
store i32 32, i32* %a_0
%b_0 = alloca i32
store i32 2, i32* %b_0
%1 = sub i32 0, 1
%d_0 = alloca i32
store i32 %1, i32* %d_0
%x_0 = alloca i32
store i32 0, i32* %x_0
store i32 0, i32* %x_0
br label %forCond1
forCond1:
%comp1_0 = alloca i32
%2 = load i32, i32* %a_0
store i32 %2, i32* %comp1_0
%3 = load i32, i32* %x_0
%4 = load i32, i32* %comp1_0
%5 = icmp sgt i32 %4, 0
br i1 %5, label %forward5, label %backward5
br label %forward5
forward5:
%7 = icmp sle i32 %3, %4
br i1 %7, label %forCode1, label %forOut1
backward5:
%8 = icmp sge i32 %3, %4
br i1 %8, label %forCode1, label %forOut1
br label %forCode1
forCode1:
%z_0 = alloca i32
store i32 0, i32* %z_0
store i32 10, i32* %z_0
br label %forCond9
forCond9:
%comp9_0 = alloca i32
%10 = sub i32 0, 1
store i32 %10, i32* %comp9_0
%11 = load i32, i32* %z_0
%12 = load i32, i32* %comp9_0
%13 = icmp sgt i32 %12, 0
br i1 %13, label %forward13, label %backward13
br label %forward13
forward13:
%15 = icmp sle i32 %11, %12
br i1 %15, label %forCode9, label %forOut9
backward13:
%16 = icmp sge i32 %11, %12
br i1 %16, label %forCode9, label %forOut9
br label %forCode9
forCode9:
%18 = load i32, i32* %z_0
call void @println(i32 %18)
%addVal18_0 = alloca i32
%19 = sub i32 0, 1
store i32 %19, i32* %addVal18_0
%20 = load i32, i32* %addVal18_0
%21 = load i32, i32* %z_0
%22 = add i32 %21, %20
store i32 %22, i32* %z_0
br label %forCond9
forOut9:
%23 = load i32, i32* %x_0
%24 = add i32 100, %23
%var_0 = alloca i32
store i32 %24, i32* %var_0
%25 = load i32, i32* %var_0
call void @println(i32 %25)
%addVal25_0 = alloca i32
%26 = load i32, i32* %b_0
store i32 %26, i32* %addVal25_0
%27 = load i32, i32* %addVal25_0
%28 = load i32, i32* %x_0
%29 = add i32 %28, %27
store i32 %29, i32* %x_0
br label %forCond1
forOut1:
ret void
}

define i32 @main(){
call void @NestForWithBackwardFor()
ret i32 0
}
