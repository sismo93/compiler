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
define void @BackwardFor(){
%1 = sub i32 0, 10
%a_0 = alloca i32
store i32 %1, i32* %a_0
%2 = sub i32 0, 2
%b_0 = alloca i32
store i32 %2, i32* %b_0
%x_0 = alloca i32
store i32 0, i32* %x_0
store i32 5, i32* %x_0
br label %forCond2
forCond2:
%comp2_0 = alloca i32
%3 = load i32, i32* %a_0
store i32 %3, i32* %comp2_0
%4 = load i32, i32* %x_0
%5 = load i32, i32* %comp2_0
%6 = icmp sgt i32 %5, 0
br i1 %6, label %forward6, label %backward6
br label %forward6
forward6:
%8 = icmp sle i32 %4, %5
br i1 %8, label %forCode2, label %forOut2
backward6:
%9 = icmp sge i32 %4, %5
br i1 %9, label %forCode2, label %forOut2
br label %forCode2
forCode2:
%11 = load i32, i32* %b_0
call void @println(i32 %11)
%addVal11_0 = alloca i32
%12 = load i32, i32* %b_0
store i32 %12, i32* %addVal11_0
%13 = load i32, i32* %addVal11_0
%14 = load i32, i32* %x_0
%15 = add i32 %14, %13
store i32 %15, i32* %x_0
br label %forCond2
forOut2:
ret void
}

define i32 @main(){
call void @BackwardFor()
ret i32 0
}
