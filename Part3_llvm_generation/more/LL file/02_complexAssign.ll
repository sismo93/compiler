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
%1 = sub i32 1, 8
%2 = mul i32 5, %1
%3 = sub i32 0, 5
%4 = mul i32 %2, %3
%5 = sub i32 %4, 0
%6 = add i32 12, 3
%7 = sub i32 %6, 5
%8 = sdiv i32 %7, 3
%9 = mul i32 17, 84
%10 = sub i32 58, 12
%11 = mul i32 15, %10
%12 = mul i32 6, %11
%13 = sub i32 %9, %12
%14 = add i32 %8, %13
%15 = add i32 %5, %14
%16 = add i32 6, %15
%v_0 = alloca i32
store i32 %16, i32* %v_0
%17 = load i32, i32* %v_0
%18 = mul i32 %17, 10
%19 = mul i32 %18, 10
%20 = mul i32 %19, 10
%21 = mul i32 %20, 10
%22 = mul i32 %21, 10
%23 = mul i32 %22, 10
%24 = mul i32 %23, 10
%25 = mul i32 %24, 10
%26 = mul i32 %25, 10
%27 = sub i32 0, 100
%28 = sub i32 0, %27
%29 = sub i32 0, %28
%30 = sub i32 0, %29
%31 = sub i32 0, %30
%32 = sub i32 0, %31
%33 = sub i32 0, %32
%34 = sub i32 0, %33
%35 = sub i32 0, %34
%36 = sub i32 0, %35
%37 = sub i32 0, %36
%38 = sub i32 0, %37
%39 = sub i32 0, %38
%40 = sub i32 0, %39
%41 = sub i32 0, %40
%42 = sub i32 0, %41
%43 = sub i32 0, %42
%44 = sub i32 0, %43
%45 = sub i32 0, %44
%46 = sub i32 0, %45
%47 = sub i32 0, %46
%48 = sub i32 0, %47
%49 = sub i32 0, %48
%50 = sub i32 0, %49
%51 = sub i32 0, %50
%52 = sub i32 0, %51
%53 = sub i32 0, %52
%54 = sub i32 0, %53
%55 = sub i32 %26, %54
%56 = add i32 %55, 15
%57 = sub i32 0, 10000
%58 = sub i32 0, %57
%59 = sub i32 0, %58
%60 = sub i32 0, %59
%61 = sub i32 0, %60
%62 = sub i32 0, %61
%63 = sub i32 0, %62
%64 = sub i32 0, %63
%65 = sub i32 0, %64
%66 = sub i32 0, %65
%67 = sub i32 0, %66
%68 = sub i32 0, %67
%69 = sub i32 0, %68
%70 = sub i32 0, %69
%71 = sub i32 0, %70
%72 = sub i32 0, %71
%73 = sub i32 0, %72
%74 = sub i32 0, %73
%75 = sub i32 0, %74
%76 = sub i32 0, %75
%77 = sub i32 0, %76
%78 = sub i32 0, %77
%79 = sub i32 0, %78
%80 = sub i32 0, %79
%81 = sub i32 0, %80
%82 = sub i32 0, %81
%83 = sub i32 0, %82
%84 = sub i32 0, %83
%85 = sub i32 0, %84
%86 = sub i32 0, %85
%87 = sub i32 0, %86
%88 = sub i32 0, %87
%89 = sub i32 0, %88
%90 = sub i32 0, %89
%91 = sub i32 0, %90
%92 = sub i32 0, %91
%93 = sub i32 0, %92
%94 = sub i32 0, %93
%95 = sub i32 0, %94
%96 = sub i32 0, %95
%97 = sub i32 0, %96
%98 = sub i32 0, %97
%99 = sub i32 0, %98
%100 = sub i32 0, %99
%101 = sub i32 0, %100
%102 = sub i32 0, %101
%103 = sub i32 0, %102
%104 = sub i32 0, %103
%105 = sub i32 0, %104
%106 = sub i32 0, %105
%107 = sub i32 0, %106
%108 = sub i32 0, %107
%109 = sub i32 0, %108
%110 = sub i32 0, %109
%111 = sub i32 0, %110
%112 = sub i32 %56, %111
%113 = add i32 %112, 111
%a_0 = alloca i32
store i32 %113, i32* %a_0
%114 = load i32, i32* %v_0
call void @println(i32 %114)
%115 = load i32, i32* %a_0
call void @println(i32 %115)
ret void
}

define i32 @main(){
call void @defaultFunctionName0()
ret i32 0
}
