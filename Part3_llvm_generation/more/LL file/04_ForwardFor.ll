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
define void @ForwardFor(){
%b_0 = alloca i32
store i32 10, i32* %b_0
%x_0 = alloca i32
store i32 0, i32* %x_0
store i32 0, i32* %x_0
br label %forCond0
forCond0:
%comp0_0 = alloca i32
%1 = load i32, i32* %b_0
store i32 %1, i32* %comp0_0
%2 = load i32, i32* %x_0
%3 = load i32, i32* %comp0_0
%4 = icmp sgt i32 %3, 0
br i1 %4, label %forward4, label %backward4
br label %forward4
forward4:
%6 = icmp sle i32 %2, %3
br i1 %6, label %forCode0, label %forOut0
backward4:
%7 = icmp sge i32 %2, %3
br i1 %7, label %forCode0, label %forOut0
br label %forCode0
forCode0:
%9 = load i32, i32* %x_0
call void @println(i32 %9)
%addVal9_0 = alloca i32
store i32 2, i32* %addVal9_0
%10 = load i32, i32* %addVal9_0
%11 = load i32, i32* %x_0
%12 = add i32 %11, %10
store i32 %12, i32* %x_0
br label %forCond0
forOut0:
ret void
}

define i32 @main(){
call void @ForwardFor()
ret i32 0
}
